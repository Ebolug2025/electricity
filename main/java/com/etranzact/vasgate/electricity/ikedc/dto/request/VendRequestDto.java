package com.lemonpay.lemonpayvas.electricity.ikedc.dto.request;

public class VendRequestDto {
    private String orderId;
    private String vendType;
    private String amount;
    private String phone;
    private String meter;
    private String disco;
    private String vertical;
    private String paymentType;

    public VendRequestDto() {
    }

    public VendRequestDto(String orderId, String vendType, String amount, String phone, String meter, String disco, String vertical, String paymentType) {
        this.orderId = orderId;
        this.vendType = vendType;
        this.amount = amount;
        this.phone = phone;
        this.meter = meter;
        this.disco = disco;
        this.vertical = vertical;
        this.paymentType = paymentType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getVendType() {
        return vendType;
    }

    public void setVendType(String vendType) {
        this.vendType = vendType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }

    public String getDisco() {
        return disco;
    }

    public void setDisco(String disco) {
        this.disco = disco;
    }

    public String getVertical() {
        return vertical;
    }

    public void setVertical(String vertical) {
        this.vertical = vertical;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}

