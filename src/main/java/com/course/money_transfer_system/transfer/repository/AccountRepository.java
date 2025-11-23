package com.course.money_transfer_system.transfer.repository;


import com.course.money_transfer_system.transfer.dto.AccountDto;
import com.course.money_transfer_system.transfer.model.Account;
import com.course.money_transfer_system.transfer.ref.CurrencyType;
import com.course.money_transfer_system.transfer.ref.TransactionStatus;
import com.course.money_transfer_system.transfer.ref.TransactionType;
import jakarta.annotation.PostConstruct;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static jooq.auth.Tables.USER_ACCOUNT;
import static org.jooq.impl.DSL.*;

import static jooq.money_transfer.Tables.*;
import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

@Repository
public class AccountRepository {
    @Autowired
    private DSLContext dsl;

    @PostConstruct
    private void fillTransactionType() {
        dsl.selectFrom(TRANSACTION_TYPE).where(TRANSACTION_TYPE.TYPE_CODE.isNotNull())
                .fetch().forEach(r -> TransactionType.fill(r.getId(), r.getTypeCode(), r.getDescription(), r.getName()));
        log.info("Transaction Type add in project");
    }

    @PostConstruct
    private void fillTransactionStatus() {
        dsl.selectFrom(TRANSACTION_STATUS).where(TRANSACTION_STATUS.STATUS_CODE.isNotNull())
                .fetch().forEach(r -> TransactionStatus.fill(r.getId(), r.getStatusCode(), r.getDescription(), r.getName()));
        log.info("Transaction Status add in project");
    }

    @PostConstruct
    private void fillCurrencyType() {
        dsl.selectFrom(CURRENCY_TYPE).where(CURRENCY_TYPE.CURRENCY_CODE.isNotNull())
                .fetch().forEach(r -> CurrencyType.fill(r.getId(), r.getCurrencyCode(), r.getDescription(), r.getName()));
        log.info("Currency add in project");
    }

    public List<AccountDto> getAccounts(Long id){
        return dsl.select(
                        ACCOUNT.ID,
                        ACCOUNT.USER_ACCOUNT_ID,
                        ACCOUNT.ACCOUNT_NUMBER,
                        ACCOUNT.CURRENCY,
                        ACCOUNT.BALANCE
                        //ACCOUNT.CREATED_AT
                )
                .from(ACCOUNT)
                .where(ACCOUNT.USER_ACCOUNT_ID.eq(id))
                .fetchInto(AccountDto.class);
    }

    public AccountDto getAccountDto(Long id){
        return dsl.select(
                    ACCOUNT.ID,
                    ACCOUNT.USER_ACCOUNT_ID,
                    ACCOUNT.ACCOUNT_NUMBER,
                    ACCOUNT.CURRENCY,
                    ACCOUNT.BALANCE
                //ACCOUNT.CREATED_AT
                )
                .from(ACCOUNT)
                .where(ACCOUNT.ID.eq(id))
                .fetchOneInto(AccountDto.class);
    }

    public Account getAccount(Long id){
        return dsl.select(
                        ACCOUNT.ID,
                        ACCOUNT.USER_ACCOUNT_ID,
                        ACCOUNT.ACCOUNT_NUMBER,
                        ACCOUNT.CURRENCY,
                        ACCOUNT.BALANCE,
                        ACCOUNT.CREATED_AT
                )
                .from(ACCOUNT)
                .where(ACCOUNT.ID.eq(id))
                .fetchOneInto(Account.class);
    }

    public Account createAccount(Account account){
        return dsl.insertInto(ACCOUNT)
                .set(dsl.newRecord(ACCOUNT, account))
                .returning()
                .fetchOneInto(Account.class);
    }

    public Account changeAccount(Account account){
        return dsl.update(ACCOUNT)
                .set(ACCOUNT.USER_ACCOUNT_ID, account.getUserAccountId())
                .set(ACCOUNT.ACCOUNT_NUMBER, account.getAccountNumber())
                .set(ACCOUNT.CURRENCY, account.getCurrency())
                //.set(ACCOUNT.BALANCE, account.getBalance())
                .set(ACCOUNT.CREATED_AT, account.getCreatedAt())
                .where(ACCOUNT.ID.eq(account.getId()))
                .returning()
                .fetchOneInto(Account.class);
    }

    public int deleteAccount(Long id){
        return dsl.deleteFrom(ACCOUNT)
                .where(ACCOUNT.ID.eq(id))
                .execute();
    }

    public boolean existAccountNumber(String accountNumber){
        return dsl.fetchExists(
                selectOne()
                        .from(ACCOUNT)
                        .where(ACCOUNT.ACCOUNT_NUMBER.eq(accountNumber.toUpperCase()))
        );
    }

    public boolean existUser(Long id){
        return dsl.fetchExists(
                selectOne()
                        .from(USER_ACCOUNT)
                        .where(USER_ACCOUNT.ID.eq(id))
        );
    }


    public Long getAccountId(String accountNumber){
        return dsl.select(ACCOUNT.ID)
                .from(ACCOUNT)
                .where(ACCOUNT.ACCOUNT_NUMBER.eq(accountNumber.toUpperCase()))
                .fetchOneInto(Long.class);
    }
}
