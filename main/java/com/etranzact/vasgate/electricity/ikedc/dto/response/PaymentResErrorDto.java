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
public class PaymentResErrorDto {
    
    private boolean success;
    private String message;

    public PaymentResErrorDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public PaymentResErrorDto() {
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

    @Override
    public String toString() {
        return "PaymentResErrorDto{" + "success=" + success + ", message=" + message + '}';
    }
    
    
    
}
