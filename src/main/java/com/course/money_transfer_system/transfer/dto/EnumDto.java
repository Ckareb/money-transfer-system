package com.course.money_transfer_system.transfer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EnumDto {
    @Schema(description = "Id")
    private Long id;

    @Schema(description = "Наименование")
    private String name;

    @Schema(description = "Описание")
    private String description;

    public EnumDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
