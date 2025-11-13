package com.course.money_transfer_system.transfer.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDto {
    private Long typeId;
    private String accountNumberFrom;
    private String accountNumberTo;
    private BigDecimal amount;
}
