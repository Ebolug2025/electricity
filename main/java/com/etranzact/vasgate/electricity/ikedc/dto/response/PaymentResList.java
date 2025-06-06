/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lemonpay.lemonpayvas.electricity.ikedc.dto.response;

import java.util.List;

/**
 *
 * @author ugochukwu.omeje
 */
public class PaymentResList {
    
    private List<PaymentResIndex> index;

    public PaymentResList() {
    }

    public List<PaymentResIndex> getIndex() {
        return index;
    }

    public void setIndex(List<PaymentResIndex> index) {
        this.index = index;
    }
    
    
}
