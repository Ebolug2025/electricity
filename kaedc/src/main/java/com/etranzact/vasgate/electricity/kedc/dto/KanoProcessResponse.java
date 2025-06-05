package com.etranzact.vasgate.electricity.kedc.dto;

public class KanoProcessResponse {
    private String transactionReference;
    private String merchantId;
    private String recieptNumber;
    private double paidamount;
    private String transactionDate;
    private String transactionStatus;
    private Customer customer;
    private String kct1;
    private String kct2;


    // Getter Methods

    public String getTransactionReference() {
        return transactionReference;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getRecieptNumber() {
        return recieptNumber;
    }

    public double getPaidamount() {
        return paidamount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    // Setter Methods

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void setRecieptNumber(String recieptNumber) {
        this.recieptNumber = recieptNumber;
    }

    public void setPaidamount(double paidamount) {
        this.paidamount = paidamount;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getKct1() { return kct1; }

    public void setKct1(String kct1) { this.kct1 = kct1; }

    public String getKct2() { return kct2; }

    public void setKct2(String kct2) { this.kct2 = kct2; }
}
