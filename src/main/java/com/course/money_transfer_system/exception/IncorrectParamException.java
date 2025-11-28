package com.course.money_transfer_system.exception;

public class IncorrectParamException extends RuntimeException {

    private String fieldOne;
    private String dateOne;
    private String fieldTwo;
    private String dateTwo;

    public IncorrectParamException() {
        super("Передан не верный параметр");
    }

    public IncorrectParamException(String message) {
        super(message);
    }

    public IncorrectParamException(String message,
                                   String date,
                                   String field) {
        super(message);
        this.dateOne = date;
        this.fieldOne = field;
    }

    public IncorrectParamException(String message,
                                   String fieldOne,
                                    String dateOne,
                                    String fieldTwo,
                                    String dateTwo) {
        super(message);
        this.dateOne = dateOne;
        this.fieldOne = fieldOne;
        this.dateTwo = dateTwo;
        this.fieldTwo = fieldTwo;
    }

    public ResponseInfo getResponseInfo(){
        return new ResponseInfo(getMessage(), dateOne, fieldOne, fieldTwo, dateTwo);
    }

}
