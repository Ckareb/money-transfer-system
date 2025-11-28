package com.course.money_transfer_system.exception;


public class EntityNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Вы не можете выполнить это действие";

    private String date;

    public EntityNotFoundException() {
        super(DEFAULT_MESSAGE);
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
