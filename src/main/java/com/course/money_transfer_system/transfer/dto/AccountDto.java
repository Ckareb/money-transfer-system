package com.course.money_transfer_system.transfer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {
    @Schema(description = "id счета")
    private Long id;

    @Schema(description = "id пользователя")
    private Long userAccountId;

    @Schema(description = "номер счета")
    private String accountNumber;

    @Schema(description = "Id валюта")
    private Long currencyId;

    @Schema(description = "Наименование валюты")
    private String currencyName;

    @Schema(description = "Описание валюты")
    private String currencyDescription;

    @Schema(description = "баланс")
    private BigDecimal balance;

    @Schema(description = "id типа счета")
    private Long typeId;

    @Schema(description = "Наименование типа счета")
    private String typeName;

    @Schema(description = "Описание типа счета")
    private String typeDescription;
}
