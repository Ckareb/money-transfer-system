package com.course.money_transfer_system.transfer.service;

import com.course.money_transfer_system.exception.AccessDeniedException;
import com.course.money_transfer_system.exception.EntityNotFoundException;
import com.course.money_transfer_system.exception.IncorrectParamException;
import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.exception.ResponseInfo;
import com.course.money_transfer_system.transfer.ref.CurrencyType;
import com.course.money_transfer_system.transfer.ref.TransactionType;
import com.course.money_transfer_system.transfer.repository.TransactionRepository;
import com.course.money_transfer_system.transfer.strategy.TransactionStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {
    private final Map<Long, TransactionStrategy> strategies = new HashMap<>();
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionService(List<TransactionStrategy> strategyList,
                              TransactionRepository transactionRepository,
                              AccountService accountService) {
       for (TransactionStrategy strategy : strategyList) {
           strategies.put(strategy.getTransactionTypeId(), strategy);
       }
       this.transactionRepository = transactionRepository;
       this.accountService = accountService;
    }

    public ResponseEntity<ResponseInfo> transaction(TransactionDto dto) {

        checkDto(dto);

        if (!canTransaction(dto))
            throw new AccessDeniedException();

        TransactionStrategy strategy = strategies.get(dto.getTypeId());
        if (strategy == null) {
            throw new EntityNotFoundException("Стратегии с данным id не существует", dto.getTypeId().toString());
        }
        return strategy.transaction(dto);
    }

    private void checkDto(TransactionDto dto){
        boolean existsTransType = false;

        for (TransactionType type : TransactionType.values()) {
            if (dto.getTypeId().equals(type.getTransactionTypeId())) {
                existsTransType = true;
                break;
            }
        }

        boolean existsCurrencyType = false;

        for (CurrencyType type : CurrencyType.values()) {
            if (dto.getCurrencyId().equals(type.getCurrencyTypeId())) {
                existsCurrencyType = true;
                break;
            }
        }

        if (dto.getTypeId() == null || !existsTransType)
            throw new IncorrectParamException("Не существует такого типа переводов", dto.getTypeId() != null ? dto.getTypeId().toString() : null, "typeId");

        if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IncorrectParamException("Сумма не может быть меньше нуля/пустым", dto.getAmount() != null ? dto.getAmount().toString() : null, "amount");

        if (dto.getNumberFrom() == null)
            throw new IncorrectParamException("Данное поле не может быть пустым", null, "numberFrom");

        if (dto.getNumberTo() == null)
            throw new IncorrectParamException("Данное поле не может быть пустым", null, "numberTo");

        if (dto.getCurrencyId() == null || !existsCurrencyType)
            throw new IncorrectParamException("Данная валюта не поддерживается", dto.getCurrencyId() != null ? dto.getCurrencyId().toString() : null, "currencyId");

        if (accountService.getAccountId(dto.getNumberFrom()) == null)
            throw new IncorrectParamException("Номер счета с которого вы собираетесь перевести введен не верно", dto.getNumberFrom(), "numberFrom");

        if (dto.getNumberTo() != null && accountService.getAccountId(dto.getNumberTo()) == null)
            throw new IncorrectParamException("Номер счета на который вы собираетесь перевести введен не верно", dto.getNumberTo(), "numberTo");

        if (!dto.getTypeId().equals(TransactionType.DEPOSIT.getTransactionTypeId())){
            if (dto.getAmount() != null && transactionRepository.balanceCheck(dto.getNumberFrom(), dto.getAmount()))
                throw new IncorrectParamException("Не достаточно средств на счете", dto.getNumberTo(),
                                                dto.getAmount().toString(), "numberTo", "amount");
        }
    }

    private boolean canTransaction(TransactionDto dto) {
        if (TransactionType.DEPOSIT.getTransactionTypeId().equals(dto.getTypeId()))

            return true;
        else
            return accountService.canTransaction(dto.getNumberFrom());
    }
}
