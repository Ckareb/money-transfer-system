package com.course.money_transfer_system.transfer.ref;

import com.course.money_transfer_system.transfer.dto.EnumDto;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

public enum AccountType {
    SETTLEMENT;


    private Long id;

    @Setter
    @Getter
    private String description;

    @Setter
    @Getter
    private String name;

    public void setAccountTypeId(Long id) {
        if (this.id == null)
            this.id = id;
    }

    @JsonValue
    public Long getAccountTypeId() {
        return id;
    }

    public static void fill(Long id, String AccountCode, String description, String name) {
        AccountType accountType = AccountType.valueOf(AccountCode.toUpperCase());
        if(accountType.getAccountTypeId() == null){
            accountType.setName(name);
            accountType.setAccountTypeId(id);
            accountType.setDescription(description);
        }
    }

    public EnumDto getEnumDto(){
        return new EnumDto(this.id, this.name, this.description);
    }
}
