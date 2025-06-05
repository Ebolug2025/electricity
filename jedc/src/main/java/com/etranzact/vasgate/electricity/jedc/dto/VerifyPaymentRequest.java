package com.etranzact.vasgate.electricity.jedc.dto;

public class VerifyPaymentRequest {

    private String accessCode;

    public VerifyPaymentRequest(String accessCode) {
        this.accessCode = accessCode;
    }

    public VerifyPaymentRequest() {
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }


}
