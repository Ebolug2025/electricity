package com.etranzact.vasgate.electricity.jedc.dto;

public class PaymentRequest
{
    private String accessCode;
    private double amount;
    private String phone;

    public PaymentRequest(String accessCode, double amount, String phone) {
        this.accessCode = accessCode;
        this.amount = amount;
        this.phone = phone;
    }

    public PaymentRequest() {
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" + "accessCode=" + accessCode + ", amount=" + amount + ", phone=" + phone + '}';
    }

}
