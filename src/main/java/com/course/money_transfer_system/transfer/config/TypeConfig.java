package com.course.money_transfer_system.transfer.config;


import com.course.money_transfer_system.transfer.model.TypeInfo;
import com.course.money_transfer_system.transfer.ref.*;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static jooq.money_transfer.Tables.*;


@Configuration
@RequiredArgsConstructor
public class TypeConfig {

    @Autowired
    private final DSLContext dsl;

    @Bean
    public TransactionTypeRegistry transactionTypeRegistry() {
        try {

            Map<String, TypeInfo> map = dsl.selectFrom(TRANSACTION_TYPE)
                    .fetch()
                    .stream()
                    .collect(Collectors.toMap(
                            r -> r.getTypeCode().toUpperCase(),
                            r -> new TypeInfo(
                                    r.getId(),
                                    r.getTypeCode(),
                                    r.getName(),
                                    r.getDescription()
                            ),
                            (a, b) -> a,
                            ConcurrentHashMap::new
                    ));

            if (map.isEmpty()) {
                throw new IllegalStateException("transaction_type table is EMPTY — startup aborted");
            }

            return new TransactionTypeRegistry(map);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to load transaction types", e);
        }
    }

    @Bean
    public CurrencyTypeRegistry currencyTypeRegistry() {
        try {

            Map<String, TypeInfo> map = dsl.selectFrom(CURRENCY_TYPE)
                    .fetch()
                    .stream()
                    .collect(Collectors.toMap(
                            r -> r.getCurrencyCode().toUpperCase(),
                            r -> new TypeInfo(
                                    r.getId(),
                                    r.getCurrencyCode(),
                                    r.getName(),
                                    r.getDescription()
                            ),
                            (a, b) -> a,
                            ConcurrentHashMap::new
                    ));

            if (map.isEmpty()) {
                throw new IllegalStateException("currency_type table is EMPTY — startup aborted");
            }

            return new CurrencyTypeRegistry(map);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to load currency types", e);
        }
    }

    @Bean
    public TransactionStatusRegistry transactionStatusRegistry() {
        try {

            Map<String, TypeInfo> map = dsl.selectFrom(TRANSACTION_STATUS)
                    .fetch()
                    .stream()
                    .collect(Collectors.toMap(
                            r -> r.getStatusCode().toUpperCase(),
                            r -> new TypeInfo(
                                    r.getId(),
                                    r.getStatusCode(),
                                    r.getName(),
                                    r.getDescription()
                            ),
                            (a, b) -> a,
                            ConcurrentHashMap::new
                    ));

            if (map.isEmpty()) {
                throw new IllegalStateException("transaction_status table is EMPTY — startup aborted");
            }

            return new TransactionStatusRegistry(map);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to load transaction status", e);
        }
    }

    @Bean
    public AccountStatusRegistry accountStatusRegistry() {
        try {

            Map<String, TypeInfo> map = dsl.selectFrom(ACCOUNT_STATUS)
                    .fetch()
                    .stream()
                    .collect(Collectors.toMap(
                            r -> r.getStatusCode().toUpperCase(),
                            r -> new TypeInfo(
                                    r.getId(),
                                    r.getStatusCode(),
                                    r.getName(),
                                    r.getDescription()
                            ),
                            (a, b) -> a,
                            ConcurrentHashMap::new
                    ));

            if (map.isEmpty()) {
                throw new IllegalStateException("account_status table is EMPTY — startup aborted");
            }

            return new AccountStatusRegistry(map);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to load account status", e);
        }
    }

    @Bean
    public AccountTypeRegistry accountTypeRegistry() {
        try {

            Map<String, TypeInfo> map = dsl.selectFrom(ACCOUNT_TYPE)
                    .fetch()
                    .stream()
                    .collect(Collectors.toMap(
                            r -> r.getTypeCode().toUpperCase(),
                            r -> new TypeInfo(
                                    r.getId(),
                                    r.getTypeCode(),
                                    r.getName(),
                                    r.getDescription()
                            ),
                            (a, b) -> a,
                            ConcurrentHashMap::new
                    ));

            if (map.isEmpty()) {
                throw new IllegalStateException("account_type table is EMPTY — startup aborted");
            }

            return new AccountTypeRegistry(map);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to load account types", e);
        }
    }
}
