package com.etranzact.vasgate.electricity.kedc.dto;

public class PaymentRequest {
    private Double amount;
    private String transaction_reference;
    private String type;
    private String payment_method;

    public PaymentRequest() {
    }

    public Double getAmount() {
        return amount;
    }

    public PaymentRequest(Double amount, String transaction_reference, String type, String payment_method) {
        this.amount = amount;
        this.transaction_reference = transaction_reference;
        this.type = type;
        this.payment_method = payment_method;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTransaction_reference() {
        return transaction_reference;
    }

    public void setTransaction_reference(String transaction_reference) {
        this.transaction_reference = transaction_reference;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "amount=" + amount +
                ", transaction_reference='" + transaction_reference + '\'' +
                ", type='" + type + '\'' +
                ", payment_method='" + payment_method + '\'' +
                '}';
    }
}
