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
public class ValidationResData {
    
    private ValidationResponse response;

    public ValidationResData() {
    }

    public ValidationResponse getResponse() {
        return response;
    }

    public void setResponse(ValidationResponse response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ValidationResData{" +
                "response=" + response +
                '}';
    }
}
