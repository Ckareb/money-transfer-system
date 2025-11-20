package com.course.money_transfer_system.transfer.ref;

import com.course.money_transfer_system.transfer.dto.EnumDto;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

public enum CurrencyType {
    RUB;


    private Long id;

    @Setter
    @Getter
    private String description;

    @Setter
    @Getter
    private String name;

    public void setCurrencyTypeId(Long id) {
        if (this.id == null)
            this.id = id;
    }

    @JsonValue
    public Long getCurrencyTypeId() {
        return id;
    }

    public static void fill(Long id, String CurrencyCode, String description, String name) {
        CurrencyType currencyType = CurrencyType.valueOf(CurrencyCode.toUpperCase());
        if(currencyType.getCurrencyTypeId() == null){
            currencyType.setName(name);
            currencyType.setCurrencyTypeId(id);
            currencyType.setDescription(description);
        }
    }

    public EnumDto getEnumDto(){
        return new EnumDto(this.id, this.name, this.description);
    }
}
