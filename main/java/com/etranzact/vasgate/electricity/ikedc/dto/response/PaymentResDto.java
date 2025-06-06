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
public class PaymentResDto {
    
    private boolean success;
    private String message;
    private PaymentResData data;
    private int status;

    //////
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PaymentResDto(boolean success, String message, PaymentResData data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public PaymentResDto(){
        
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

    public PaymentResData getData() {
        return data;
    }

    public void setData(PaymentResData data) {
        this.data = data;
    }
    
    
    
}
