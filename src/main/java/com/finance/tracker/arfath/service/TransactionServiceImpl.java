package com.finance.tracker.arfath.service;

import com.finance.tracker.arfath.model.Transaction;
import com.finance.tracker.arfath.repository.TransactionRepository;
import com.finance.tracker.arfath.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    // Placeholder: Implement this method to get current logged-in user ID
    private Long getCurrentUserId() {

        return 1L;
    }

    @Override
    @Transactional(readOnly = true)
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }

    @Override
    public List<Transaction> getAllTransactionsForUser() {
        return transactionRepository.findAll(); // Instead of filtering by userId
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
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
