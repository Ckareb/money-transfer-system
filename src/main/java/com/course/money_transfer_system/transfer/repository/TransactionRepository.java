package com.course.money_transfer_system.transfer.repository;

import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.transfer.dto.TransactionHistoryDto;
import com.course.money_transfer_system.transfer.model.TransactionHistory;
import com.course.money_transfer_system.transfer.ref.TransactionStatus;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static jooq.money_transfer.Tables.*;

@Repository
public class TransactionRepository {
    @Autowired
    private DSLContext dsl;

    public void transactionSubtract(TransactionDto dto){
        dsl.update(ACCOUNT)
                .set(ACCOUNT.BALANCE, ACCOUNT.BALANCE.subtract(dto.getAmount()))
                .where(ACCOUNT.ACCOUNT_NUMBER.eq(dto.getNumberFrom().toUpperCase()))
                .execute();
    }

    public void transactionAdd(TransactionDto dto){
        dsl.update(ACCOUNT)
                .set(ACCOUNT.BALANCE, ACCOUNT.BALANCE.add(dto.getAmount()))
                .where(ACCOUNT.ACCOUNT_NUMBER.eq(dto.getNumberTo().toUpperCase()))
                .execute();
    }

    public boolean balanceCheck(String accountNumber, BigDecimal amount){
        BigDecimal balance = dsl.select(ACCOUNT.BALANCE)
                .from(ACCOUNT)
                .where(ACCOUNT.ACCOUNT_NUMBER.eq(accountNumber.toUpperCase()))
                .fetchOneInto(BigDecimal.class);

        //balance не может быть null в БД
        BigDecimal newBalance = balance.subtract(amount);

        return newBalance.compareTo(BigDecimal.ZERO) < 0;
    }
}
