package com.course.money_transfer_system.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserAccessDto {
    @Schema(description = "Просмотр")
    private boolean read;

    @Schema(description = "Создание")
    private boolean create;

    @Schema(description = "Изменение")
    private boolean change;

    @Schema(description = "Удаление")
    private boolean delete;
}
