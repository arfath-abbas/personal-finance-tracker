package com.finance.tracker.arfath.service.serviceImpl;

import com.finance.tracker.arfath.model.Transaction;
import com.finance.tracker.arfath.repository.TransactionRepository;
import com.finance.tracker.arfath.repository.UserRepository;
import com.finance.tracker.arfath.security.services.UserDetailsImpl;
import com.finance.tracker.arfath.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    // Get current logged-in user ID from Spring Security context
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userDetails.getId();
        }
        // Return null or a default user ID if not authenticated
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }

    @Override
    @Transactional
    public List<Transaction> getAllTransactionsForUser() {
        Long userId = getCurrentUserId();
        if (userId != null) {
            return transactionRepository.findByUserId(userId);
        } else {
            // For public access, return empty list
            return new ArrayList<>();
        }
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        // Set the current authenticated user to the transaction if available
        Long userId = getCurrentUserId();
        if (userId != null) {
            userRepository.findById(userId).ifPresent(transaction::setUser);
        }
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction updateTransaction(Long id, Transaction transactionRequest) {
        Transaction existingTransaction = getTransactionById(id);

        existingTransaction.setAmount(transactionRequest.getAmount());
        existingTransaction.setType(transactionRequest.getType());
        existingTransaction.setCategory(transactionRequest.getCategory());
        existingTransaction.setDescription(transactionRequest.getDescription());
        existingTransaction.setTransactionDate(transactionRequest.getTransactionDate());

        return transactionRepository.save(existingTransaction);
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
