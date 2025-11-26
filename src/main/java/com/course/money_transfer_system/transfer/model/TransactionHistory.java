package com.course.money_transfer_system.transfer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionHistory {
    @Schema(description = "Id")
    private Long id;

    @Schema(description = "Id счета откуда переводят")
    private Long fromAccountId;

    @Schema(description = "Id счета куда переводят")
    private Long toAccountId;

    @Schema(description = "Сумма транзакции")
    private BigDecimal amount;

    @Schema(description = "Id типа валюты")
    private Long currency;

    @Schema(description = "Id типа транзакции")
    private Long typeId;

    @Schema(description = "Id статуса транзакции")
    private Long statusId;

    @Schema(description = "Дата создания транзакции")
    private LocalDateTime createdAt;

    @Schema(description = "Описание")
    private String description;

    @Schema(description = "Куда отправляются деньги вне системы")
    private String outgoingCode;

    @Schema(description = "Наименование статуса транзакции")
    private String statusName;

    @Schema(description = "Наименование валюты транзакции")
    private String currencyName;

    @Schema(description = "Наименование типа транзакции")
    private String typeName;


    public TransactionHistory(Long id,
                              Long fromAccountId,
                              Long toAccountId,
                              BigDecimal amount,
                              Long currency,
                              Long typeId,
                              Long statusId,
                              LocalDateTime createdAt,
                              String description,
                              String outgoingCode,
                              String statusName,
                              String currencyName,
                              String typeName) {
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currency = currency;
        this.typeId = typeId;
        this.statusId = statusId;
        this.createdAt = createdAt;
        this.description = description;
        this.outgoingCode = outgoingCode;
        this.statusName = statusName;
        this.currencyName = currencyName;
        this.typeName = typeName;
    }
}
