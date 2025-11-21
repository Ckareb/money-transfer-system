package com.course.money_transfer_system.exception;

import com.course.money_transfer_system.transfer.model.ResponseInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<ResponseInfo> handlerEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        ResponseInfo info = ex.getResponseInfo();
        info.setPublicErrorInfo(request, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectParamException.class)
    ResponseEntity<ResponseInfo> handlerIncorrectParamException(IncorrectParamException ex, HttpServletRequest request) {
        ResponseInfo info = ex.getResponseInfo();
        info.setPublicErrorInfo(request, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }
}
