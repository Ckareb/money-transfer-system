package com.course.money_transfer_system.transfer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AccountModifyAccessDto {
    @Schema(description = "Просмотр")
    private boolean readList;

    @Schema(description = "Изменение")
    private boolean change;

    @Schema(description = "Удаление")
    private boolean delete;
}
