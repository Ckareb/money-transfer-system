package com.course.money_transfer_system.exception;

import com.course.money_transfer_system.transfer.model.ResponseInfo;


public class EntityNotFoundException extends RuntimeException {

    private String date;

    public EntityNotFoundException() {
        super("Значение не найдено");
    }


    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message,
                                   String date) {
        super(message);
        this.date = date;
    }

    public ResponseInfo getResponseInfo() {
        return new ResponseInfo(getMessage(), date);
    }
}
