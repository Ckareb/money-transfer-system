package com.course.money_transfer_system.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserDto {
    @Schema(description = "id пользователя")
    private Long id;

    @Schema(description = "Имя пользователя")
    private String username;

    @Schema(description = "Пароль пользователя")
    private String password;

    @Schema(description = "Id роли пользователя")
    private Long roleId;

    @Schema(description = "Данные пользователя")
    private UserDataDto userDataDto;
}
