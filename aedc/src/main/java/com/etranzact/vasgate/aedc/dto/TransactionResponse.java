package com.etranzact.vasgate.aedc.dto;

public class TransactionResponse
{
    private String receipt_no;
    private String reference;
    private String customer_no;
    private String customer_name;
    private String customer_address;
    private String vendor;
    private Double amount;
    private String vref;
    private Integer code;
    private String message;

    public TransactionResponse(String receipt_no, String reference, String customer_no, String customer_name, String customer_address, String vendor, Double amount, String vref, Integer code, String message) {
        this.receipt_no = receipt_no;
        this.reference = reference;
        this.customer_no = customer_no;
        this.customer_name = customer_name;
        this.customer_address = customer_address;
        this.vendor = vendor;
        this.amount = amount;
        this.vref = vref;
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "TransactionResponse{" + "receipt_no=" + receipt_no + ", reference=" + reference + ", customer_no=" + customer_no + ", customer_name=" + customer_name + ", customer_address=" + customer_address + ", vendor=" + vendor + ", amount=" + amount + ", vref=" + vref + ", code=" + code + ", message=" + message + '}';
    }

    public String getReceipt_no() {
        return receipt_no;
    }

    public void setReceipt_no(String receipt_no) {
        this.receipt_no = receipt_no;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCustomer_no() {
        return customer_no;
    }

    public void setCustomer_no(String customer_no) {
        this.customer_no = customer_no;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getVref() {
        return vref;
    }

    public void setVref(String vref) {
        this.vref = vref;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
