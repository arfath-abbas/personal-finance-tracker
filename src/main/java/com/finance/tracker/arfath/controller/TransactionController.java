package com.finance.tracker.arfath.controller;

import com.finance.tracker.arfath.model.Transaction;
import com.finance.tracker.arfath.model.dto.TransactionDTO;
import com.finance.tracker.arfath.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // Create a new transaction
    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody Transaction transactionRequest) {
        Transaction createdTransaction = transactionService.createTransaction(transactionRequest);
        return ResponseEntity.ok(TransactionDTO.fromTransaction(createdTransaction));
    }

    // Get all transactions for the authenticated user
    @GetMapping("/getAll")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactionsForUser();
        List<TransactionDTO> dtos = transactions.stream()
                .map(TransactionDTO::fromTransaction)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Get specific transaction by ID
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(TransactionDTO.fromTransaction(transaction));
    }

    // Update an existing transaction
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long id, @RequestBody Transaction transactionRequest) {
        Transaction updatedTransaction = transactionService.updateTransaction(id, transactionRequest);
        return ResponseEntity.ok(TransactionDTO.fromTransaction(updatedTransaction));
    }

    // Delete a transaction
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("Record deleted successfully");
    }
}