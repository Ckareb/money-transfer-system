package com.course.money_transfer_system.transfer.ref;

import com.course.money_transfer_system.transfer.dto.EnumDto;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

public enum TransactionType {
    TRANSFER,
    PAYMENT,
    WITHDRAW,
    DEPOSIT;


    private Long id;

    @Setter
    @Getter
    private String description;

    @Setter
    @Getter
    private String name;


    public void setTransactionTypeId(Long id) {
        if (this.id == null)
            this.id = id;
    }

    @JsonValue
    public Long getTransactionTypeId() {
        return id;
    }

    public static void fill(Long id, String typeCode, String description, String name) {
        TransactionType transactionType = TransactionType.valueOf(typeCode.toUpperCase());
        if(transactionType.getTransactionTypeId() == null){
            transactionType.setTransactionTypeId(id);
            transactionType.setDescription(description);
            transactionType.setName(name);
        }
    }

    public EnumDto getEnumDto(){
        return new EnumDto(this.id, this.name, this.description);
    }
}
