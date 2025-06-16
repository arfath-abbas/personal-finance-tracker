package com.finance.tracker.arfath.model.dto;

import com.finance.tracker.arfath.model.Transaction;
import com.finance.tracker.arfath.utils.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private Long id;
    private Long userId;
    private String username;
    private BigDecimal amount;
    private TransactionType type;
    private String category;
    private String description;
    private LocalDate transactionDate;
    private LocalDateTime createdAt;
    
    public static TransactionDTO fromTransaction(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        if (transaction.getUser() != null) {
            dto.setUserId(transaction.getUser().getId());
            dto.setUsername(transaction.getUser().getUsername());
        }
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setCategory(transaction.getCategory());
        dto.setDescription(transaction.getDescription());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setCreatedAt(transaction.getCreatedAt());
        return dto;
    }
}