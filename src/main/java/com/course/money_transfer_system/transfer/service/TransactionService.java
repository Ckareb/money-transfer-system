package com.course.money_transfer_system.transfer.service;

import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.transfer.strategy.TransactionStrategy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {
    private final Map<Long, TransactionStrategy> strategies = new HashMap<>();

    public TransactionService(List<TransactionStrategy> strategyList) {
       for (TransactionStrategy strategy : strategyList) {
           strategies.put(strategy.getTransactionTypeId(), strategy);
       }
    }

    public void transaction(TransactionDto dto) {
        TransactionStrategy strategy = strategies.get(dto.getTypeId());
        if (strategy == null) {
            //TODO исключение
            throw new RuntimeException("Стратегии с данным id " + dto.getTypeId() + " не существует");
        }
        strategy.transaction(dto);
    }
}
