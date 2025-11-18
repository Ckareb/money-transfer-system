package com.course.money_transfer_system.transfer.service;

import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.transfer.model.TransactionHistory;
import com.course.money_transfer_system.transfer.ref.TransactionStatus;
import com.course.money_transfer_system.transfer.ref.TransactionType;
import com.course.money_transfer_system.transfer.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionHistoryService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionHistoryService(TransactionRepository tr,
                                     AccountService accountService) {
        this.transactionRepository = tr;
        this.accountService = accountService;
    }

    @Transactional
    public void createTransactionHistory(TransactionDto dto,
                                  Long fromId,
                                  Long toId,
                                  TransactionType type,
                                  String terminalCode) {

        TransactionHistory history = new TransactionHistory(
                null,
                fromId,
                toId,
                dto.getAmount(),
                dto.getCurrencyId(),
                type.getTransactionTypeId(),
                TransactionStatus.FAILED.getTransactionStatusId(),
                LocalDateTime.now(),
                type.getDescription(),
                terminalCode
        );

        transactionRepository.transactionHistoryInsert(history);
    }

    @Transactional
    public void transactionHistoryChangeStatus(Long id) {
        transactionRepository.transactionHistoryChangeStatus(id);
    }


    public Long getAccountId(String accountNumber) {
        Long accountId = accountService.getAccountId(accountNumber);
        //TODO Сделать исключение
        if (accountId == null)
            return null;
        return accountId;
    }
}
