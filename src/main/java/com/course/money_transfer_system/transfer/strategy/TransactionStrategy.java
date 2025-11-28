package com.course.money_transfer_system.transfer.strategy;

import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.exception.ResponseInfo;
import org.springframework.http.ResponseEntity;

public interface TransactionStrategy {
    Long getTransactionTypeId();
    ResponseEntity<ResponseInfo> transaction(TransactionDto dto);
}
