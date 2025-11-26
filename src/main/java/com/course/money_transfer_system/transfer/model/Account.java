package com.course.money_transfer_system.transfer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Account {
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

    @Schema(description = "дата создания")
    private LocalDateTime createdAt;

    @Schema(description = "id типа счета")
    private Long typeId;

    @Schema(description = "id статуса счета")
    private Long statusId;
}
