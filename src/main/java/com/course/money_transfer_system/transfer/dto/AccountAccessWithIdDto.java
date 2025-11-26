package com.course.money_transfer_system.transfer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AccountAccessWithIdDto {
    @Schema(description = "Просмотр")
    private boolean read;

    @Schema(description = "Использование средств")
    private boolean transfer;

    @Schema(description = "Изменение")
    private boolean change;

    @Schema(description = "Удаление")
    private boolean delete;
}
