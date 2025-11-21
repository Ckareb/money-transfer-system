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

    @Schema(description = "валюта")
    private Long currency;

    @Schema(description = "баланс")
    private BigDecimal balance;
}
