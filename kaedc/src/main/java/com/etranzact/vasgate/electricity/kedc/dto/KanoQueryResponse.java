package com.etranzact.vasgate.electricity.kedc.dto;

public class KanoQueryResponse {
    private String customerName;
    private String meterNumber;
    private String accountNumber;
    private String businessUnit;
    private String undertaking;
    private String address;
    private String phoneNumber;
    private String email;
    private String lastTransactionDate;
    private float minimumPurchase;
    private float customerArrears;
    private String tariffCode;
    private String tariff;


    // Getter Methods

    public String getCustomerName() {
        return customerName;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public String getUndertaking() {
        return undertaking;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getLastTransactionDate() {
        return lastTransactionDate;
    }

    public float getMinimumPurchase() {
        return minimumPurchase;
    }

    public float getCustomerArrears() {
        return customerArrears;
    }

    public String getTariffCode() {
        return tariffCode;
    }

    public String getTariff() {
        return tariff;
    }

    // Setter Methods

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public void setUndertaking(String undertaking) {
        this.undertaking = undertaking;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastTransactionDate(String lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public void setMinimumPurchase(float minimumPurchase) {
        this.minimumPurchase = minimumPurchase;
    }

    public void setCustomerArrears(float customerArrears) {
        this.customerArrears = customerArrears;
    }

    public void setTariffCode(String tariffCode) {
        this.tariffCode = tariffCode;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }
}
