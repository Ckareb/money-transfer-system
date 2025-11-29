package com.course.money_transfer_system.transfer.model;

import com.course.money_transfer_system.transfer.dto.EnumDto;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

public class TypeInfo {
    @Setter
    private Long id;


    @Getter
    private final String code;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private String description;

    public TypeInfo(Long id, String code, String name, String description) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
    }

    @JsonValue
    public Long getId() {
        return id;
    }

    public EnumDto getEnumDto() {
        return new EnumDto(id, name, description);
    }
}
