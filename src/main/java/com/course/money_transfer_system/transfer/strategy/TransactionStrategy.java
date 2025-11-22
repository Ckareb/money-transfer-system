package com.course.money_transfer_system.transfer.strategy;

import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.transfer.model.ResponseInfo;
import com.course.money_transfer_system.transfer.service.TransactionService;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface TransactionStrategy {
    Long getTransactionTypeId();
    ResponseEntity<ResponseInfo> transaction(TransactionDto dto);
}
