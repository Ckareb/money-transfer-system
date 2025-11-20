package com.course.money_transfer_system.transfer.model;


import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Data
public class ResponseInfo {
    private String message;
    private LocalDateTime data;
    private String description;
    private String status;
    private String field;
    private String date;
    //private HttpStatus httpStatus;
    //private HttpStatusCode httpStatusCode;

    public ResponseInfo(String message,
                        LocalDateTime data){
        this.message = message;
        this.data = data;
    }

    public ResponseInfo(String message,
                        String field,
                        LocalDateTime data){
        this.message = message;
        this.data = data;
        this.field = field;
    }

    public ResponseInfo(String message,
                        String date,
                        String field,
                        LocalDateTime data){
        this.message = message;
        this.date = date;
        this.field = field;
        this.data = data;
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
}
