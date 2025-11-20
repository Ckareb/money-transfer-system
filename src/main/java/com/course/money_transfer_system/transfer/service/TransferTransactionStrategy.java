package com.course.money_transfer_system.transfer.service;

import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.transfer.model.ResponseInfo;
import com.course.money_transfer_system.transfer.ref.TransactionStatus;
import com.course.money_transfer_system.transfer.ref.TransactionType;
import com.course.money_transfer_system.transfer.repository.TransactionRepository;
import com.course.money_transfer_system.transfer.strategy.TransactionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class TransferTransactionStrategy implements TransactionStrategy {

    private final TransactionRepository transactionRepository;
    private final TransactionHistoryService transactionHistoryService;

    @Autowired
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
    public ResponseEntity<ResponseInfo> transaction(TransactionDto dto){
        transactionHistoryService.createTransactionHistory(
                dto,
                dto.getNumberFrom(),
                dto.getNumberTo(),
                TransactionType.TRANSFER,
                null
        );

        //checkBalance(dto.getNumberFrom(), dto.getAmount());

        transactionRepository.transactionSubtract(dto);

        transactionRepository.transactionAdd(dto);

        transactionHistoryService.transactionHistoryChangeStatus(TransactionStatus.SUCCESS.getTransactionStatusId());

        return new ResponseEntity<>(
                new ResponseInfo(
                        "Перевод совершен успешно на счет " + dto.getNumberTo(),
                        LocalDateTime.now(),
                        TransactionStatus.SUCCESS.getDescription()
                ), HttpStatus.OK);
    }

//    private void checkBalance(String accountNumber, BigDecimal amount){
//        if (!transactionRepository.balanceCheck(accountNumber, amount)){
//            //TODO исключение
//            System.out.println("Не достаточно средств на счете");
//        }
//    }
}
