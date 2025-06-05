package com.etranzact.vasgate.aedc.dto;

public class PaymentValueRequest {
    private String idVendor;
    private String codUser;
    private String meterSerial;
    private String account;
    private Double totalPayment;
    private Double debtPayment;

    public String getIdVendor() {
        return idVendor;
    }

    public PaymentValueRequest setIdVendor(String idVendor) {
        this.idVendor = idVendor;
        return this;
    }

    public String getCodUser() {
        return codUser;
    }

    public PaymentValueRequest setCodUser(String codUser) {
        this.codUser = codUser;
        return this;
    }

    public String getMeterSerial() {
        return meterSerial;
    }

    public PaymentValueRequest setMeterSerial(String meterSerial) {
        this.meterSerial = meterSerial;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public PaymentValueRequest setAccount(String account) {
        this.account = account;
        return this;
    }

    public Double getTotalPayment() {
        return totalPayment;
    }

    public PaymentValueRequest setTotalPayment(Double totalPayment) {
        this.totalPayment = totalPayment;
        return this;
    }

    public Double getDebtPayment() {
        return debtPayment;
    }

    public PaymentValueRequest setDebtPayment(Double debtPayment) {
        this.debtPayment = debtPayment;
        return this;
    }

    @Override
    public String toString() {
        return "PaymentValueRequest{" +
                "idVendor='" + idVendor + '\'' +
                ", codUser='" + codUser + '\'' +
                ", meterSerial='" + meterSerial + '\'' +
                ", account='" + account + '\'' +
                ", totalPayment=" + totalPayment +
                ", debtPayment=" + debtPayment +
                '}';
    }
}
