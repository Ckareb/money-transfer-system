package com.course.money_transfer_system.transfer.service;

import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.transfer.model.TransactionHistory;
import com.course.money_transfer_system.transfer.ref.TransactionStatus;
import com.course.money_transfer_system.transfer.ref.TransactionType;
import com.course.money_transfer_system.transfer.repository.TransactionRepository;
import com.course.money_transfer_system.transfer.strategy.TransactionStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class PaymentTransactionStrategy implements TransactionStrategy {

    private final TransactionRepository transactionRepository;

    public PaymentTransactionStrategy(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Long getTransactionTypeId(){
        return TransactionType.PAYMENT.getTransactionTypeId();
    }

    public void transaction(TransactionDto dto){
        checkBalance(dto.getAccountNumberFrom(), dto.getAmount());
        transactionRepository.transactionSubtract(dto);
        //TODO привязать
        transactionRepository.transactionHistoryInsert(
                new TransactionHistory(null,
                        getAccountId(dto.getAccountNumberFrom()),
                        getAccountId(dto.getAccountNumberTo()),
                        dto.getAmount(),
                        "RUB",
                        TransactionType.TRANSFER.getTransactionTypeId(),
                        TransactionStatus.SUCCESS.getTransactionStatusId(),
                        LocalDateTime.now(),
                        TransactionType.TRANSFER.getDescription())
        );
    }

    public void checkBalance(String accountNumber, BigDecimal amount){
        if (!transactionRepository.balanceCheck(accountNumber, amount)){
            //TODO исключение
            System.out.println("Не достаточно средств на счете");
        }
    }

    private Long getAccountId(String accountNumber){
        return transactionRepository.getAccountId(accountNumber);
    }
}
