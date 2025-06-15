package com.finance.tracker.arfath.repository;

import com.finance.tracker.arfath.model.Transaction;
import com.finance.tracker.arfath.utils.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    // Total Income/Expense between given dates
    @Query("SELECT SUM(t.amount) FROM Transaction t " +
            "WHERE (t.user IS NULL OR t.user.id = :userId) AND t.type = :type " +
            "AND t.transactionDate BETWEEN :start AND :end")
    BigDecimal getTotalByType(Long userId, TransactionType type, LocalDate start, LocalDate end);

    // Daily Spending for bar chart
    @Query("SELECT EXTRACT(DAY FROM t.transactionDate), SUM(t.amount) FROM Transaction t " +
            "WHERE (t.user IS NULL OR t.user.id = :userId) AND t.type = 'EXPENSE' " +
            "AND t.transactionDate BETWEEN :start AND :end " +
            "GROUP BY EXTRACT(DAY FROM t.transactionDate) ORDER BY 1")
    List<Object[]> getDailySpending(Long userId, LocalDate start, LocalDate end);

    // Spending by category for category graph
    @Query("SELECT t.category, SUM(t.amount) FROM Transaction t " +
            "WHERE (t.user IS NULL OR t.user.id = :userId) AND t.transactionDate BETWEEN :start AND :end " +
            "GROUP BY t.category ORDER BY SUM(t.amount) DESC")
    List<Object[]> getSpendingByCategory(Long userId, LocalDate start, LocalDate end);
}
