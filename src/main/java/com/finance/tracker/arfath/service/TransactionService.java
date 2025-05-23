package com.finance.tracker.arfath.service;

import com.finance.tracker.arfath.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {

     List<Transaction> getAllTransactionsForUser();

     Transaction getTransactionById(Long id);

     Transaction createTransaction(Transaction transaction);

     Transaction updateTransaction(Long id, Transaction transaction);

     void deleteTransaction(Long id);
}
