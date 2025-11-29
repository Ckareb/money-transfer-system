package com.course.money_transfer_system.transfer.service;

import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.exception.ResponseInfo;
import com.course.money_transfer_system.transfer.ref.TransactionStatusRegistry;
import com.course.money_transfer_system.transfer.ref.TransactionTypeRegistry;
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
    private final TransactionTypeRegistry transactionTypeRegistry;
    private final TransactionStatusRegistry transactionStatusRegistry;

    public DepositTransactionStrategy(TransactionRepository transactionRepository,
                                      TransactionHistoryService transactionHistoryService,
                                      TransactionTypeRegistry transactionTypeRegistry,
                                      TransactionStatusRegistry transactionStatusRegistry) {
        this.transactionRepository = transactionRepository;
        this.transactionHistoryService = transactionHistoryService;
        this.transactionTypeRegistry = transactionTypeRegistry;
        this.transactionStatusRegistry = transactionStatusRegistry;
    }

    public Long getTransactionTypeId(){
        return transactionTypeRegistry.get("DEPOSIT").getId();
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseInfo> transaction(TransactionDto dto){
        transactionHistoryService.createTransactionHistory(
                dto,
                null,
                dto.getNumberTo(),
                transactionTypeRegistry.get("DEPOSIT"),
                dto.getNumberFrom()
        );

        transactionRepository.transactionAdd(dto);

        transactionHistoryService.transactionHistoryChangeStatus(transactionStatusRegistry.get("SUCCESS").getId());

        return new ResponseEntity<>(
                new ResponseInfo(
                "Счет на сумму " + dto.getAmount() + " пополнен успешно",
                LocalDateTime.now(),
                transactionStatusRegistry.get("SUCCESS").getDescription()
        ), HttpStatus.OK);
    }
}
