package com.lemonpay.lemonpayvas.electricity.ikedc.dto.response;

public class QueryFailed {

    Boolean status;
    Boolean error;
    int responseCode;
    String message;
    boolean success;

    public QueryFailed() {
    }

    public QueryFailed(Boolean status, Boolean error, int responseCode, String message, boolean success) {
        this.status = status;
        this.error = error;
        this.responseCode = responseCode;
        this.message = message;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "QueryFailed{" +
                "status=" + status +
                ", error=" + error +
                ", responseCode=" + responseCode +
                ", message='" + message + '\'' +
                '}';
    }
}
