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
public class PaymentResIndex {
    
    private String feeType;
    private String feeAmount;

    public PaymentResIndex(String feeType, String feeAmount) {
        this.feeType = feeType;
        this.feeAmount = feeAmount;
    }

    public PaymentResIndex() {
    }
        
    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }
    
    
}
