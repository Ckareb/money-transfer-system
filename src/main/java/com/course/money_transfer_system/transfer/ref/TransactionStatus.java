package com.course.money_transfer_system.transfer.ref;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

public enum TransactionStatus {
    SUCCESS,
    FAILED;


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

    public static void fill(Long id, String typeCode, String description) {
        TransactionStatus transactionStatus = TransactionStatus.valueOf(typeCode.toUpperCase());
        if(transactionStatus.getTransactionStatusId() == null){
            transactionStatus.setTransactionStatusId(id);
            transactionStatus.setDescription(description);
        }
    }
}
