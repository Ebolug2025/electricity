package com.lemonpay.lemonpayvas.electricity.dto;

public enum ResponseEnum {
    SUCCESSFUL("200","Request has been received and processed. Response will contain expected parameters in body"),
    SUCCESSFUL_CREATION("201","Records have been created successfully"),
    BAD_REQUEST("400","Bad request"),
    UNAUTHORIZED("401","Authorization failed. Endpoint requires authorization."),
    FORBIDDEN("403","Access to endpoint is either restricted or some other limitation is in place"),
    NOT_FOUND("404","Non_existent data was requested")
    ;


    private String responseCode;
    private String responseMessage;

    private ResponseEnum(String code, String message){
        this.responseMessage = message;
        this.responseCode = code;
    }

    public String getResponseCode(){
        return responseCode;
    }
    public String getResponseMessage(){
        return responseMessage;
    }
}
