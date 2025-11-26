package com.course.money_transfer_system.transfer.ref;

import com.course.money_transfer_system.transfer.dto.EnumDto;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

public enum AccountStatus {
    ACTIVE,
    CLOSED,
    BLOCKED;


    private Long id;

    @Setter
    @Getter
    private String description;

    @Setter
    @Getter
    private String name;

    public void setAccountStatusId(Long id) {
        if (this.id == null)
            this.id = id;
    }

    @JsonValue
    public Long getAccountStatusId() {
        return id;
    }

    public static void fill(Long id, String StatusCode, String description, String name) {
        AccountStatus accountType = AccountStatus.valueOf(StatusCode.toUpperCase());
        if(accountType.getAccountStatusId() == null){
            accountType.setName(name);
            accountType.setAccountStatusId(id);
            accountType.setDescription(description);
        }
    }
}
