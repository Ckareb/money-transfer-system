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

    public void setTransactionStatusId(Long id) {
        if (this.id == null)
            this.id = id;
    }

    @JsonValue
    public Long getTransactionStatusId() {
        return id;
    }

    public static void fill(Long id, String CurrencyCode, String description) {
        CurrencyType transactionStatus = CurrencyType.valueOf(CurrencyCode.toUpperCase());
        if(transactionStatus.getTransactionStatusId() == null){
            transactionStatus.setTransactionStatusId(id);
            transactionStatus.setDescription(description);
        }
    }

    public EnumDto getEnumDto(){
        return new EnumDto(this.id, null, this.description);
    }
}
