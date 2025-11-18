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
    private Long currency;
    private Long typeId;
    private Long statusId;
    private LocalDateTime createdAt;
    private String description;
    private String terminalCode;

    public TransactionHistory(Long id,
                              Long fromAccountId,
                              Long toAccountId,
                              BigDecimal amount,
                              Long currency,
                              Long typeId,
                              Long statusId,
                              LocalDateTime createdAt,
                              String description,
                              String terminalCode) {
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currency = currency;
        this.typeId = typeId;
        this.statusId = statusId;
        this.createdAt = createdAt;
        this.description = description;
        this.terminalCode = terminalCode;
    }
}
