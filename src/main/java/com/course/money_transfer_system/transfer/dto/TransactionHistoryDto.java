package com.course.money_transfer_system.transfer.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionHistoryDto {
    private Long id;
    private Long fromAccountId;
    private String fromAccountNumber;
    private Long toAccountId;
    private String toAccountNumber;
    private Long amount;
    private Long currencyId;
    private String currencyName;
    private Long typeId;
    private Long statusId;
    private LocalDateTime createdAt;
    private String description;
    private String terminalCode;
}
