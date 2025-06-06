/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lemonpay.lemonpayvas.electricity.ikedc.dto.request;

/**
 *
 * @author ugochukwu.omeje
 */
public class PaymentReqDto {
    
    private String customerPhoneNumber;
    private String paymentMethod;
    private String service;
    private String clientReference;
    private String pin;
    private String productCode;
    private PaymentCard card;

    public PaymentReqDto(String customerPhoneNumber, String paymentMethod, String service, String clientReference, String pin, String productCode, PaymentCard card) {
        this.customerPhoneNumber = customerPhoneNumber;
        this.paymentMethod = paymentMethod;
        this.service = service;
        this.clientReference = clientReference;
        this.pin = pin;
        this.productCode = productCode;
        this.card = card;
    }
    
    public PaymentReqDto(){
        
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getClientReference() {
        return clientReference;
    }

    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public PaymentCard getCard() {
        return card;
    }

    public void setCard(PaymentCard card) {
        this.card = card;
    }
    
    
}
