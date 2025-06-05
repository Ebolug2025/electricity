package com.etranzact.vasgate.aedc.dto;

public class PaymentDetailsRequest {
    private String idVendor;
    private String codUser;
    private String transactionId;

    public String getIdVendor() {
        return idVendor;
    }

    public PaymentDetailsRequest setIdVendor(String idVendor) {
        this.idVendor = idVendor;
        return this;
    }

    public String getCodUser() {
        return codUser;
    }

    public PaymentDetailsRequest setCodUser(String codUser) {
        this.codUser = codUser;
        return this;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public PaymentDetailsRequest setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    @Override
    public String toString() {
        return "PaymentDetailsRequest{" +
                "idVendor='" + idVendor + '\'' +
                ", codUser='" + codUser + '\'' +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
