package com.course.money_transfer_system.transfer.repository;

import com.course.money_transfer_system.transfer.dto.TransactionHistoryDto;
import com.course.money_transfer_system.transfer.model.TransactionHistory;
import com.course.money_transfer_system.transfer.ref.TransactionStatusRegistry;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static jooq.money_transfer.Tables.*;

@Repository
public class TransactionHistoryRepository {
    @Autowired
    private DSLContext dsl;

    private final TransactionStatusRegistry transactionStatusRegistry;
    public TransactionHistoryRepository(TransactionStatusRegistry transactionStatusRegistry) {
        this.transactionStatusRegistry = transactionStatusRegistry;
    }

    public Long countByAccountId(Long accountId) {
        return dsl.selectCount()
                .from(TRANSACTION_HISTORY)
                .where(TRANSACTION_HISTORY.FROM_ACCOUNT_ID.eq(accountId)
                        .or(TRANSACTION_HISTORY.TO_ACCOUNT_ID.eq(accountId)))
                .fetchOne(0, Long.class);
    }

    public List<TransactionHistoryDto> getTransactionHistories(Long accountId, int offset, int limit){

        return dsl.select(
                    TRANSACTION_HISTORY.ID,
                    TRANSACTION_HISTORY.FROM_ACCOUNT_ID,
                    ACCOUNT.as("fromAccount").ACCOUNT_NUMBER.as("fromAccountNumber"),
                    TRANSACTION_HISTORY.TO_ACCOUNT_ID,
                    ACCOUNT.as("toAccount").ACCOUNT_NUMBER.as("toAccountNumber"),
                    TRANSACTION_HISTORY.AMOUNT,
                    TRANSACTION_HISTORY.CURRENCY.as("currencyId"),
                    CURRENCY_TYPE.NAME.as("currencyName"),
                    TRANSACTION_HISTORY.TYPE_ID,
                    TRANSACTION_HISTORY.STATUS_ID,
                    TRANSACTION_HISTORY.CREATED_AT,
                    TRANSACTION_HISTORY.DESCRIPTION,
                    TRANSACTION_HISTORY.OUTGOING_CODE
                )
                .from(TRANSACTION_HISTORY)
                .join(ACCOUNT.as("fromAccount")).on(TRANSACTION_HISTORY.FROM_ACCOUNT_ID.eq(ACCOUNT.as("fromAccount").ID))
                .join(ACCOUNT.as("toAccount")).on(TRANSACTION_HISTORY.TO_ACCOUNT_ID.eq(ACCOUNT.as("toAccount").ID))
                .join(CURRENCY_TYPE).on(TRANSACTION_HISTORY.CURRENCY.eq(CURRENCY_TYPE.ID))
                .where(TRANSACTION_HISTORY.FROM_ACCOUNT_ID.eq(accountId)
                        .or(TRANSACTION_HISTORY.TO_ACCOUNT_ID.eq(accountId))
                ).and(TRANSACTION_HISTORY.STATUS_ID.eq(transactionStatusRegistry.get("SUCCESS").getId()))
                .offset(offset).limit(limit)
                .fetchInto(TransactionHistoryDto.class);


    }

    public void transactionHistoryInsert(TransactionHistory history){
        dsl.insertInto(TRANSACTION_HISTORY)
                .set(dsl.newRecord(TRANSACTION_HISTORY, history))
                .execute();
    }

    public void transactionHistoryChangeStatus(Long statusId, String statusName){
        dsl.update(TRANSACTION_HISTORY)
                .set(TRANSACTION_HISTORY.STATUS_ID, statusId)
                .set(TRANSACTION_HISTORY.STATUS_NAME, statusName)
                .execute();
    }
}
