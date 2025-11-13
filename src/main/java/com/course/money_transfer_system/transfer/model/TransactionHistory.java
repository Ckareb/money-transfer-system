package com.course.money_transfer_system.transfer.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionHistory {
    private Long id;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private String currency;
    private Long typeId;
    private Long statusId;
    private LocalDateTime createdAt;
    private String description;

    public TransactionHistory(Long id,
                              Long fromAccountId,
                              Long toAccountId,
                              BigDecimal amount,
                              String currency,
                              Long typeId,
                              Long statusId,
                              LocalDateTime createdAt,
                              String description) {
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currency = currency;
        this.typeId = typeId;
        this.statusId = statusId;
        this.createdAt = createdAt;
        this.description = description;
    }
}
