package com.finance.tracker.arfath.controller;

import com.finance.tracker.arfath.model.Transaction;
import com.finance.tracker.arfath.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // Create a new transaction
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transactionRequest) {
        Transaction createdTransaction = transactionService.createTransaction(transactionRequest);
        return ResponseEntity.ok(createdTransaction);
    }

    // Get all transactions for the authenticated user
    @GetMapping("/getAll")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactionsForUser();
        return ResponseEntity.ok(transactions);
    }

    // Get specific transaction by ID
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    // Update an existing transaction
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction transactionRequest) {
        Transaction updatedTransaction = transactionService.updateTransaction(id, transactionRequest);
        return ResponseEntity.ok(updatedTransaction);
    }

    // Delete a transaction
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("Record deleted successfully");
    }
}