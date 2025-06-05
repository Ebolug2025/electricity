package com.etranzact.vasgate.electricity.kedc.dto;

public class PaymentResponse {
    private Boolean success;
    private String message;
    private ProcessPayload payload;
    private String err_message;

    public PaymentResponse() {
    }

    public PaymentResponse(Boolean success, String message, ProcessPayload processPayload, String err_message) {
        this.success = success;
        this.message = message;
        payload = processPayload;
        err_message = err_message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErr_message() {
        return err_message;
    }

    public void setErr_message(String err_message) {
        this.err_message = err_message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProcessPayload getPayload() {
        return payload;
    }

    public void setPayload(ProcessPayload payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "PaymentResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", ProcessPayload=" + payload +
                ", err_message='" + err_message + '\'' +
                '}';
    }
}
