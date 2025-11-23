package com.course.money_transfer_system.transfer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionHistoryDto {
    @Schema(description = "Id")
    private Long id;

    @Schema(description = "Id счета откуда переводят")
    private Long fromAccountId;

    @Schema(description = "Номер счета откуда переводят")
    private String fromAccountNumber;

    @Schema(description = "Id счета куда переводят")
    private Long toAccountId;

    @Schema(description = "Номер счета куда переводят")
    private String toAccountNumber;

    @Schema(description = "Сумма транзакции")
    private Long amount;

    @Schema(description = "Id типа валюты")
    private Long currencyId;

    @Schema(description = "Наименование типа валюты")
    private String currencyName;

    @Schema(description = "Id типа транзакции")
    private Long typeId;

    @Schema(description = "Id статуса транзакции")
    private Long statusId;

    @Schema(description = "Дата создания транзакции")
    private LocalDateTime createdAt;

    @Schema(description = "Описание")
    private String description;

    @Schema(description = "Код терминала используемого при транзакции")
    private String terminalCode;
}
