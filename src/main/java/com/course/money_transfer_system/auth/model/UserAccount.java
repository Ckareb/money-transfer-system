package com.course.money_transfer_system.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserAccount {
    @Schema(description = "Id")
    private Long id;

    @Schema(description = "Наименование пользователя")
    private String username;

    @Schema(description = "Пароль пользователя")
    private String password;

    @Schema(description = "Включено?")
    private boolean enabled;

    @Schema(description = "Id роли")
    private Long roleId;
}
