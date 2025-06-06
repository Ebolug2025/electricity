/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lemonpay.lemonpayvas.electricity.ikedc.dto.response;

/**
 *
 * @author ugochukwu.omeje
 */
public class ValidationResDto {
    
    public ValidationResDto(){
        
    }
    
    private boolean success;
    private String message;
    private ValidationResData data;

    public ValidationResDto(boolean success, String message, ValidationResData data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ValidationResData getData() {
        return data;
    }

    public void setResponse(ValidationResData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ValidationResDto{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
