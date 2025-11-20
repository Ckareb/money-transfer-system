package com.course.money_transfer_system.transfer.service;

import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.transfer.model.TransactionHistory;
import com.course.money_transfer_system.transfer.ref.TransactionStatus;
import com.course.money_transfer_system.transfer.ref.TransactionType;
import com.course.money_transfer_system.transfer.repository.TransactionRepository;
import com.course.money_transfer_system.transfer.strategy.TransactionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class WithdrawTransactionStrategy implements TransactionStrategy {

    private final TransactionRepository transactionRepository;
    private final TransactionHistoryService transactionHistoryService;

    @Autowired
    public WithdrawTransactionStrategy(TransactionRepository transactionRepository,
                                       TransactionHistoryService transactionHistoryService) {
        this.transactionRepository = transactionRepository;
        this.transactionHistoryService = transactionHistoryService;
    }

    public Long getTransactionTypeId(){
        return TransactionType.WITHDRAW.getTransactionTypeId();
    }

    @Override
    @Transactional
    public void transaction(TransactionDto dto){
        transactionHistoryService.createTransactionHistory(
                dto,
                transactionHistoryService.getAccountId(dto.getNumberFrom()),
                null,
                TransactionType.WITHDRAW,
                dto.getNumberTo()
                );
        checkBalance(dto.getNumberFrom(), dto.getAmount());

        transactionRepository.transactionSubtract(dto);
        transactionHistoryService.transactionHistoryChangeStatus(TransactionStatus.SUCCESS.getTransactionStatusId());
    }


    private void checkBalance(String accountNumber, BigDecimal amount){
        if (!transactionRepository.balanceCheck(accountNumber, amount)){
            //TODO исключение
            System.out.println("Не достаточно средств на счете");
        }
    }
}
