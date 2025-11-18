package com.course.money_transfer_system.transfer.dto;

import lombok.Data;

@Data
public class EnumDto {
    private Long id;
    private String name;
    private String description;

    public EnumDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
