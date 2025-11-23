package com.course.money_transfer_system.transfer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDto {
    @Schema(description = "Id типа транзакции")
    private Long typeId;

    @Schema(description = "Номер откуда переводим")
    private String numberFrom;

    @Schema(description = "Номер куда переводим")
    private String numberTo;

    @Schema(description = "Сумма транзакции")
    private BigDecimal amount;

    @Schema(description = "Id типа валюты")
    private Long currencyId;
}
