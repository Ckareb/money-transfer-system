package com.course.money_transfer_system.transfer.repository;


import com.course.money_transfer_system.transfer.dto.AccountDto;
import com.course.money_transfer_system.transfer.model.Account;
import com.course.money_transfer_system.transfer.ref.*;
import jakarta.annotation.PostConstruct;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    @PostConstruct
    private void fillAccountType() {
        dsl.selectFrom(ACCOUNT_TYPE).where(ACCOUNT_TYPE.TYPE_CODE.isNotNull())
                .fetch().forEach(r -> AccountType.fill(r.getId(), r.getTypeCode(), r.getDescription(), r.getName()));
        log.info("Account type add in project");
    }

    @PostConstruct
    private void fillAccountStatus() {
        dsl.selectFrom(ACCOUNT_STATUS).where(ACCOUNT_STATUS.STATUS_CODE.isNotNull())
                .fetch().forEach(r -> AccountStatus.fill(r.getId(), r.getStatusCode(), r.getDescription(), r.getName()));
        log.info("Account status add in project");
    }

    public List<AccountDto> getAccounts(Long userId){
        return dsl.select(
                        ACCOUNT.ID,
                        ACCOUNT.USER_ACCOUNT_ID,
                        ACCOUNT.ACCOUNT_NUMBER,
                        ACCOUNT.CURRENCY.as("currencyId"),
                        CURRENCY_TYPE.NAME.as("currencyName"),
                        CURRENCY_TYPE.DESCRIPTION.as("currencyDescription"),
                        ACCOUNT.BALANCE,
                        ACCOUNT.TYPE_ID.as("typeId"),
                        ACCOUNT_TYPE.NAME.as("typeName"),
                        ACCOUNT_TYPE.DESCRIPTION.as("typeDescription")
                )
                .from(ACCOUNT)
                .join(ACCOUNT_TYPE).on(ACCOUNT.TYPE_ID.eq(ACCOUNT_TYPE.ID))
                .join(CURRENCY_TYPE).on(ACCOUNT.CURRENCY.eq(CURRENCY_TYPE.ID))
                .where(ACCOUNT.USER_ACCOUNT_ID.eq(userId))
                .and(ACCOUNT.STATUS_ID.eq(AccountStatus.ACTIVE.getAccountStatusId()))
                .fetchInto(AccountDto.class);
    }

    public AccountDto getAccountDto(Long id){
        return dsl.select(
                    ACCOUNT.ID,
                    ACCOUNT.USER_ACCOUNT_ID,
                    ACCOUNT.ACCOUNT_NUMBER,
                    ACCOUNT.CURRENCY,
                    ACCOUNT.BALANCE
                )
                .from(ACCOUNT)
                .where(ACCOUNT.ID.eq(id))
                .and(ACCOUNT.STATUS_ID.eq(AccountStatus.ACTIVE.getAccountStatusId()))
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
                .and(ACCOUNT.STATUS_ID.eq(AccountStatus.ACTIVE.getAccountStatusId()))
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
                .set(ACCOUNT.ACCOUNT_NUMBER, account.getAccountNumber())
                .set(ACCOUNT.CURRENCY, account.getCurrency())
                .set(ACCOUNT.TYPE_ID, account.getTypeId())
                .set(ACCOUNT.CREATED_AT, account.getCreatedAt())
                .where(ACCOUNT.ID.eq(account.getId()))
                .returning()
                .fetchOneInto(Account.class);
    }

    public int closeAccount(Long id){
        return dsl.update(ACCOUNT)
                .set(ACCOUNT.STATUS_ID, AccountStatus.CLOSED.getAccountStatusId())
                .set(ACCOUNT.CLOSED_AT, LocalDateTime.now())
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


    public Long getAccountId(String accountNumber){
        return dsl.select(ACCOUNT.ID)
                .from(ACCOUNT)
                .where(ACCOUNT.ACCOUNT_NUMBER.eq(accountNumber.toUpperCase()))
                .fetchOneInto(Long.class);
    }
}
