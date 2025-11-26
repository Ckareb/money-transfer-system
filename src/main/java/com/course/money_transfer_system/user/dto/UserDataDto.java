package com.course.money_transfer_system.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDataDto {
    @Schema(description = "ID записи user_data")
    private Long id;

    @Schema(description = "ID аккаунта пользователя")
    private Long userId;

    @Schema(description = "ФИО пользователя")
    private String fullName;

    @Schema(description = "Дата рождения")
    private LocalDate birthDate;

    @Schema(description = "Страна")
    private String country;

    @Schema(description = "Номер телефона")
    private String numberPhone;

    @Schema(description = "Серия паспорта")
    private String passportSeries;

    @Schema(description = "Номер паспорта")
    private String passportNumber;

    @Schema(description = "Кем выдан паспорт")
    private String passportIssuedBy;

    @Schema(description = "Дата выдачи паспорта")
    private LocalDate passportIssueDate;
}
