package com.course.money_transfer_system.transfer.repository;

import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.transfer.model.TransactionHistory;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import static jooq.money_transfer.Tables.ACCOUNT;
import static jooq.money_transfer.Tables.TRANSACTION_HISTORY;
import static org.jooq.impl.DSL.selectOne;

@Repository
public class TransactionRepository {
    @Autowired
    private DSLContext dsl;

    public Long getAccountId(String accountNumber){
        return dsl.select(ACCOUNT.ID)
                        .from(ACCOUNT)
                        .where(ACCOUNT.ACCOUNT_NUMBER.eq(accountNumber.toUpperCase()))
                        .fetchOneInto(Long.class);
    }

    public void transactionSubtract(TransactionDto dto){
        dsl.update(ACCOUNT)
                .set(ACCOUNT.BALANCE, ACCOUNT.BALANCE.subtract(dto.getAmount()))
                .where(ACCOUNT.ACCOUNT_NUMBER.eq(dto.getAccountNumberFrom().toUpperCase()))
                .execute();;
    }

    public void transactionAdd(TransactionDto dto){
        dsl.update(ACCOUNT)
                .set(ACCOUNT.BALANCE, ACCOUNT.BALANCE.add(dto.getAmount()))
                .where(ACCOUNT.ACCOUNT_NUMBER.eq(dto.getAccountNumberTo().toUpperCase()))
                .execute();;
    }

    public void transactionHistoryInsert(TransactionHistory history){
        dsl.insertInto(TRANSACTION_HISTORY)
                .set(dsl.newRecord(TRANSACTION_HISTORY, history))
                .execute();;
    }

    public boolean balanceCheck(String accountNumber, BigDecimal amount){
        BigDecimal balance = dsl.select(ACCOUNT.BALANCE)
                .from(ACCOUNT)
                .where(ACCOUNT.ACCOUNT_NUMBER.eq(accountNumber.toUpperCase()))
                .fetchOneInto(BigDecimal.class);
        if (amount == null || balance == null)
            return false;

        BigDecimal newBalance = balance.subtract(amount);

        if (newBalance == null) {
            //TODO исключение
            System.out.println("Счёт не найден: " + accountNumber);
            return false;
        }

        return newBalance.compareTo(BigDecimal.ZERO) >= 0;
    }
}
