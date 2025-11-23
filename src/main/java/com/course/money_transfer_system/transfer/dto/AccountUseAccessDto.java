package com.course.money_transfer_system.transfer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AccountUseAccessDto {
    @Schema(description = "Просмотр")
    private boolean read;

    @Schema(description = "Использование средств")
    private boolean transfer;
}
