package com.course.money_transfer_system.transfer.service;

import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.exception.ResponseInfo;
import com.course.money_transfer_system.transfer.ref.TransactionStatus;
import com.course.money_transfer_system.transfer.ref.TransactionType;
import com.course.money_transfer_system.transfer.repository.TransactionRepository;
import com.course.money_transfer_system.transfer.strategy.TransactionStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class DepositTransactionStrategy implements TransactionStrategy {

    private final TransactionRepository transactionRepository;
    private final TransactionHistoryService transactionHistoryService;

    public DepositTransactionStrategy(TransactionRepository transactionRepository,
                                      TransactionHistoryService transactionHistoryService) {
        this.transactionRepository = transactionRepository;
        this.transactionHistoryService = transactionHistoryService;
    }

    public Long getTransactionTypeId(){
        return TransactionType.DEPOSIT.getTransactionTypeId();
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseInfo> transaction(TransactionDto dto){
        transactionHistoryService.createTransactionHistory(
                dto,
                null,
                dto.getNumberTo(),
                TransactionType.DEPOSIT,
                dto.getNumberFrom()
        );

        transactionRepository.transactionAdd(dto);

        transactionHistoryService.transactionHistoryChangeStatus(TransactionStatus.SUCCESS.getTransactionStatusId());

        return new ResponseEntity<>(
                new ResponseInfo(
                "Счет на сумму " + dto.getAmount() + " пополнен успешно",
                LocalDateTime.now(),
                TransactionStatus.SUCCESS.getDescription()
        ), HttpStatus.OK);
    }
}
