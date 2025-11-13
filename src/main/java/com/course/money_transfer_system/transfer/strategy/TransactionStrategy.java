package com.course.money_transfer_system.transfer.strategy;

import com.course.money_transfer_system.transfer.dto.TransactionDto;

import java.math.BigDecimal;

public interface TransactionStrategy {
    Long getTransactionTypeId();
    void transaction(TransactionDto dto);
    void checkBalance(String accountNumber, BigDecimal amount);
}
