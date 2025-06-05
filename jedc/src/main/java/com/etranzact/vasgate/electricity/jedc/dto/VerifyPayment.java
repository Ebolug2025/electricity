package com.etranzact.vasgate.electricity.jedc.dto;

public class VerifyPayment {

    private String status;
    private String accessCode;
    private String message;
    private VerifyPaymentDetailsResponse payDetails;

    public VerifyPayment(String status, String accessCode, String message, VerifyPaymentDetailsResponse payDetails) {
        this.status = status;
        this.accessCode = accessCode;
        this.message = message;
        this.payDetails = payDetails;
    }

    public VerifyPayment() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public VerifyPaymentDetailsResponse getPayDetails() {
        return payDetails;
    }

    public void setPayDetails(VerifyPaymentDetailsResponse payDetails) {
        this.payDetails = payDetails;
    }

    @Override
    public String toString() {
        return "VerifyPayment{" + "status=" + status + ", accessCode=" + accessCode + ", message=" + message + ", payDetails=" + payDetails + '}';
    }


}
