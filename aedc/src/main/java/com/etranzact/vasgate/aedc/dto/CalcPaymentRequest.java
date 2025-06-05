package com.etranzact.vasgate.aedc.dto;

public class CalcPaymentRequest
{
    private String idVendor;
    private String codUser;
    private String meterSerial;
    private String account;
    private Double totalPayment;
    private Double debtPayment;

    public CalcPaymentRequest() {}
    public CalcPaymentRequest(String idVendor, String codUser, String meterSerial, String account,
                              Double totalPayment, Double debtPayment) {
        this.idVendor = idVendor;
        this.codUser = codUser;
        this.meterSerial = meterSerial;
        this.account = account;
        this.totalPayment = totalPayment;
        this.debtPayment = debtPayment;
    }

    public String getIdVendor() {
        return idVendor;
    }

    public CalcPaymentRequest setIdVendor(String idVendor) {
        this.idVendor = idVendor;
        return this;
    }

    public String getCodUser() {
        return codUser;
    }

    public CalcPaymentRequest setCodUser(String codUser) {
        this.codUser = codUser;
        return this;
    }

    public String getMeterSerial() {
        return meterSerial;
    }

    public CalcPaymentRequest setMeterSerial(String meterSerial) {
        this.meterSerial = meterSerial;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public CalcPaymentRequest setAccount(String account) {
        this.account = account;
        return this;
    }

    public Double getTotalPayment() {
        return totalPayment;
    }

    public CalcPaymentRequest setTotalPayment(Double totalPayment) {
        this.totalPayment = totalPayment;
        return this;
    }

    public Double getDebtPayment() {
        return debtPayment;
    }

    public CalcPaymentRequest setDebtPayment(Double debtPayment) {
        this.debtPayment = debtPayment;
        return this;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "idVendor='" + idVendor + '\'' +
                ", codUser='" + codUser + '\'' +
                ", meterSerial='" + meterSerial + '\'' +
                ", account='" + account + '\'' +
                ", totalPayment=" + totalPayment +
                ", debtPayment=" + debtPayment +
                '}';
    }
}
