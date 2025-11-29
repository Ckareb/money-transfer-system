package com.course.money_transfer_system.transfer.service;

import com.course.money_transfer_system.exception.IncorrectParamException;
import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.transfer.dto.TransactionHistoryDto;
import com.course.money_transfer_system.transfer.model.TransactionHistory;
import com.course.money_transfer_system.transfer.model.TypeInfo;
import com.course.money_transfer_system.transfer.ref.*;
import com.course.money_transfer_system.transfer.repository.TransactionHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionHistoryService {

    private final TransactionHistoryRepository transactionHistoryRepository;
    private final AccountService accountService;
    private final TransactionTypeRegistry transactionTypeRegistry;
    private final CurrencyTypeRegistry currencyTypeRegistry;
    private final TransactionStatusRegistry transactionStatusRegistry;

    public TransactionHistoryService(TransactionHistoryRepository transactionHistoryRepository,
                                     AccountService accountService,
                                     TransactionTypeRegistry transactionTypeRegistry,
                                     CurrencyTypeRegistry currencyTypeRegistry,
                                     TransactionStatusRegistry transactionStatusRegistry) {
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.accountService = accountService;
        this.transactionTypeRegistry = transactionTypeRegistry;
        this.currencyTypeRegistry = currencyTypeRegistry;
        this.transactionStatusRegistry = transactionStatusRegistry;
    }

    /**
     * Получает историю переводов
     * @param accountId id аккаунта
     * @param pageable паджинация
     * @return историю переводов
     */
    public Page<TransactionHistoryDto> getTransactionHistoryPages(Long accountId, Pageable pageable){

        Long total = transactionHistoryRepository.countByAccountId(accountId);

        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();

        List<TransactionHistoryDto> pageContent = transactionHistoryRepository.getTransactionHistories(accountId, offset, limit);

        return new PageImpl<>(pageContent, pageable, total);
    }

    @Transactional
    public void createTransactionHistory(TransactionDto dto,
                                  String fromId,
                                  String toId,
                                  TypeInfo type,
                                  String outgoingCode) {

        String currencyName = null;
        for (TypeInfo currencyType : currencyTypeRegistry.values()) {
            if (currencyType.getId().equals(dto.getCurrencyId())) {
                currencyName = currencyType.getName();
            }
        }

        if (currencyName == null) {
            throw new IncorrectParamException("Данный тип валюты не имеет названия " + dto.getCurrencyId());
        }

        String typeName = null;
        for (TypeInfo transactionType : transactionTypeRegistry.values()) {
            if (transactionType.getId().equals(dto.getTypeId())) {
                typeName = transactionType.getName();
            }
        }

        if (typeName == null) {
            throw new IncorrectParamException("Данный тип транзакции не имеет названия " + dto.getCurrencyId());
        }

        TransactionHistory history = new TransactionHistory(
                null,
                fromId == null ? null : accountService.getAccountId(fromId),
                toId == null ? null : accountService.getAccountId(toId),
                dto.getAmount(),
                dto.getCurrencyId(),
                type.getId(),
                transactionStatusRegistry.get("FAILED").getId(),
                LocalDateTime.now(),
                type.getDescription(),
                outgoingCode,
                transactionStatusRegistry.get("FAILED").getName(),
                currencyName,
                typeName
        );

        transactionHistoryRepository.transactionHistoryInsert(history);
    }

    @Transactional
    public void transactionHistoryChangeStatus(Long id) {
        String nameStatus = null;
        for (TypeInfo status : transactionStatusRegistry.values()) {
            if (status.getId().equals(id)) {
                nameStatus = status.getName();
            }
        }

        if (nameStatus == null) {
            throw new IncorrectParamException("Данный тип транзакции не имеет названия " + id);
        }

        transactionHistoryRepository.transactionHistoryChangeStatus(id, nameStatus);
    }

}
