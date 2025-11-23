package com.course.money_transfer_system.exception;

import com.course.money_transfer_system.transfer.model.ResponseInfo;


public class AccessDeniedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Вы не можете выполнить это действие";

    public AccessDeniedException() {
        super(DEFAULT_MESSAGE);
    }

    public AccessDeniedException(String message) {
        super(message);
    }

    public ResponseInfo getResponseInfo() {
        return new ResponseInfo(getMessage());
    }
}
