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
public class EncPinResDto {
    
    private String responseCode;
    private String message;
    private EncDataResDto data;
    
    public EncPinResDto(){
        
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EncDataResDto getData() {
        return data;
    }

    public void setData(EncDataResDto data) {
        this.data = data;
    }
    
    
}
