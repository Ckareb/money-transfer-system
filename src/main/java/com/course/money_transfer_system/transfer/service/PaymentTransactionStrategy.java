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
public class PaymentTransactionStrategy implements TransactionStrategy {

    private final TransactionRepository transactionRepository;
    private final TransactionHistoryService transactionHistoryService;

    public PaymentTransactionStrategy(TransactionRepository transactionRepository,
                                       TransactionHistoryService transactionHistoryService) {
        this.transactionRepository = transactionRepository;
        this.transactionHistoryService = transactionHistoryService;
    }

    public Long getTransactionTypeId(){
        return TransactionType.PAYMENT.getTransactionTypeId();
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseInfo> transaction(TransactionDto dto){
        transactionHistoryService.createTransactionHistory(
                dto,
                dto.getNumberFrom(),
                null,
                TransactionType.PAYMENT,
                dto.getNumberTo()
        );
        //checkBalance(dto.getNumberFrom(), dto.getAmount());

        transactionRepository.transactionSubtract(dto);
        transactionHistoryService.transactionHistoryChangeStatus(TransactionStatus.SUCCESS.getTransactionStatusId());

        return new ResponseEntity<>(
                new ResponseInfo(
                        "Оплачено успешно на сумму " + dto.getAmount(),
                        LocalDateTime.now(),
                        TransactionStatus.SUCCESS.getDescription()
                ), HttpStatus.OK);
    }
}
