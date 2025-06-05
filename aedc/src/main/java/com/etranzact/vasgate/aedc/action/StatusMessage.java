package com.etranzact.vasgate.aedc.action;

public enum StatusMessage {

    INVALID_METER_TYPE("05"," invalid meter type");

    public String responseCode;
    public String responseMsg;
    StatusMessage(String responseCode, String responseMsg){

        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
    }
}
