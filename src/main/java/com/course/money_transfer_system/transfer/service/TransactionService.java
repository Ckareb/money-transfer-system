package com.course.money_transfer_system.transfer.service;

import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.transfer.model.ResponseInfo;
import com.course.money_transfer_system.transfer.ref.CurrencyType;
import com.course.money_transfer_system.transfer.ref.TransactionType;
import com.course.money_transfer_system.transfer.repository.TransactionRepository;
import com.course.money_transfer_system.transfer.strategy.TransactionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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
        TransactionStrategy strategy = strategies.get(dto.getTypeId());
        if (strategy == null) {
            //TODO исключение
            throw new RuntimeException("Стратегии с данным id " + dto.getTypeId() + " не существует");
        }
        return strategy.transaction(dto);
    }

    private void checkDto(TransactionDto dto){
        for (TransactionType type : TransactionType.values())
            if (dto.getTypeId() == null || !dto.getTypeId().equals(type.getTransactionTypeId()))
                System.out.println("Не достаточно средств на счете");

        if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            System.out.println("Не достаточно средств на счете");

        if (dto.getNumberFrom() == null)
            System.out.println("Не достаточно средств на счете");

        if (dto.getNumberTo() == null)
            System.out.println("Не достаточно средств на счете");

        for (CurrencyType type : CurrencyType.values())
            if (dto.getCurrencyId() == null || !dto.getCurrencyId().equals(type.getCurrencyTypeId()))
                System.out.println("Не достаточно средств на счете");

        if (dto.getNumberFrom() != null && accountService.getAccountId(dto.getNumberFrom()) == null)
            System.out.println("Не достаточно средств на счете");

        if (dto.getNumberTo() != null && accountService.getAccountId(dto.getNumberTo()) == null)
            System.out.println("Не достаточно средств на счете");

        if (dto.getTypeId().equals(TransactionType.DEPOSIT.getTransactionTypeId())){
            if (dto.getAmount() != null && transactionRepository.balanceCheck(dto.getNumberFrom(), dto.getAmount()))
                //TODO исключение
                System.out.println("Не достаточно средств на счете");
        }

    }
}
