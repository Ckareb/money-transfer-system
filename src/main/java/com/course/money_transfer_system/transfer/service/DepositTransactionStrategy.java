package com.course.money_transfer_system.transfer.service;

import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.transfer.ref.TransactionStatus;
import com.course.money_transfer_system.transfer.ref.TransactionType;
import com.course.money_transfer_system.transfer.repository.TransactionRepository;
import com.course.money_transfer_system.transfer.strategy.TransactionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DepositTransactionStrategy implements TransactionStrategy {

    private final TransactionRepository transactionRepository;
    private final TransactionHistoryService transactionHistoryService;

    @Autowired
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
    public void transaction(TransactionDto dto){
        transactionHistoryService.createTransactionHistory(
                dto,
                null,
                transactionHistoryService.getAccountId(dto.getNumberTo()),
                TransactionType.DEPOSIT,
                dto.getNumberFrom()
        );

        transactionRepository.transactionAdd(dto);

        transactionHistoryService.transactionHistoryChangeStatus(TransactionStatus.SUCCESS.getTransactionStatusId());
    }
}
