package com.course.money_transfer_system.user.ref;

import com.course.money_transfer_system.transfer.dto.EnumDto;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

public enum RoleType {
    USER,
    ADMIN;


    private Long id;

    @Setter
    @Getter
    private String description;

    public void setRoleTypeId(Long id) {
        if (this.id == null)
            this.id = id;
    }

    @JsonValue
    public Long getRoleTypeId() {
        return id;
    }

    public static void fill(Long id, String AccountCode, String description) {
        RoleType accountType = RoleType.valueOf(AccountCode.toUpperCase());
        if(accountType.getRoleTypeId() == null){
            accountType.setRoleTypeId(id);
            accountType.setDescription(description);
        }
    }
}
