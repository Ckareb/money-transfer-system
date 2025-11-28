package com.course.money_transfer_system.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResponseInfo {
    private String message;
    private LocalDateTime data;
    private String description;
    private String status;
    private String fieldOne;
    private String dateOne;
    private String fieldTwo;
    private String dateTwo;
    private int httpStatus;
    private HttpStatusCode httpStatusCode;
    private String path;

    public ResponseInfo(String message){
        this.message = message;
    }

    public ResponseInfo(String message,
                        String date){
        this.message = message;
        this.dateOne = date;
    }

    public ResponseInfo(String message,
                        LocalDateTime data){
        this.message = message;
        this.data = data;
    }

    public ResponseInfo(String message,
                        String date,
                        String field){
        this.message = message;
        this.dateOne = date;
        this.fieldOne = field;
    }

    public ResponseInfo(String message,
                        String dateOne,
                        String fieldOne,
                        String dateTwo,
                        String fieldTwo){
        this.message = message;
        this.dateOne = dateOne;
        this.fieldOne = fieldOne;
        this.dateTwo = dateTwo;
        this.fieldTwo = fieldTwo;
    }

    public ResponseInfo(String message,
                        LocalDateTime data,
                        String description){
        this.message = message;
        this.data = data;
        this.description = description;
    }



    public ResponseInfo(String message,
                        LocalDateTime data,
                        String description,
                        String status){
        this.message = message;
        this.data = data;
        this.description = description;
        this.status = status;
    }

    public void setPublicErrorInfo(HttpServletRequest request, HttpStatus status) {
        this.data = LocalDateTime.now();
        this.httpStatus = status.value();
        this.httpStatusCode = HttpStatus.valueOf(status.value());
        this.path = request.getRequestURI();
    }
}
