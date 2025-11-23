package com.course.money_transfer_system.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Role {
    @Schema(description = "Id")
    private int id;

    @Schema(description = "Системное наименование")
    private String sysName;

    @Schema(description = "Описание")
    private String description;

    @Schema(description = "Актуальная роль?")
    private boolean isActual;
}
