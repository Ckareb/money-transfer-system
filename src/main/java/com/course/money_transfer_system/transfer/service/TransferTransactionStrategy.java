package com.course.money_transfer_system.transfer.service;

import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.transfer.ref.TransactionStatus;
import com.course.money_transfer_system.transfer.ref.TransactionType;
import com.course.money_transfer_system.transfer.repository.TransactionRepository;
import com.course.money_transfer_system.transfer.strategy.TransactionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class TransferTransactionStrategy implements TransactionStrategy {

    private final TransactionRepository transactionRepository;
    private final TransactionHistoryService transactionHistoryService;

    public TransferTransactionStrategy(TransactionRepository transactionRepository,
                                      TransactionHistoryService transactionHistoryService) {
        this.transactionRepository = transactionRepository;
        this.transactionHistoryService = transactionHistoryService;
    }

    public Long getTransactionTypeId(){
        return TransactionType.TRANSFER.getTransactionTypeId();
    }

    @Override
    @Transactional
    public void transaction(TransactionDto dto){
        transactionHistoryService.createTransactionHistory(
                dto,
                transactionHistoryService.getAccountId(dto.getNumberFrom()),
                transactionHistoryService.getAccountId(dto.getNumberTo()),
                TransactionType.TRANSFER,
                null
        );
        checkBalance(dto.getNumberFrom(), dto.getAmount());
        transactionRepository.transactionSubtract(dto);
        transactionRepository.transactionAdd(dto);
        transactionHistoryService.transactionHistoryChangeStatus(TransactionStatus.SUCCESS.getTransactionStatusId());
    }

    private void checkBalance(String accountNumber, BigDecimal amount){
        if (!transactionRepository.balanceCheck(accountNumber, amount)){
            //TODO исключение
            System.out.println("Не достаточно средств на счете");
        }
    }
}
