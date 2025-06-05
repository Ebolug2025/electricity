package com.etranzact.vasgate.electricity.jedc.dto;

public class PaymentResponse
{
    private String status;
    private String message;
    private String accessCode;

    public PaymentResponse() {
    }

    public PaymentResponse(String status, String message, String accessCode) {
        this.status = status;
        this.message = message;
        this.accessCode = accessCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    @Override
    public String toString() {
        return "PaymentResponse{" + "status=" + status + ", message=" + message + ", accessCode=" + accessCode + '}';
    }

}
