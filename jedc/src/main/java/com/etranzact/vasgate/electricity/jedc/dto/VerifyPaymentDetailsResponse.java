package com.etranzact.vasgate.electricity.jedc.dto;

public class VerifyPaymentDetailsResponse {

    private String transactionID;
    private String accountNumber;
    private String meterNumber;
    private String amount;
    private String token;
    private String units;
    private String tariffRate;
    private String vat;
    private String outstandingPaid;

    public VerifyPaymentDetailsResponse(String transactionID, String accountNumber, String meterNumber, String amount, String token, String units, String tariffRate, String vat, String outstandingPaid) {
        this.transactionID = transactionID;
        this.accountNumber = accountNumber;
        this.meterNumber = meterNumber;
        this.amount = amount;
        this.token = token;
        this.units = units;
        this.tariffRate = tariffRate;
        this.vat = vat;
        this.outstandingPaid = outstandingPaid;
    }

    public VerifyPaymentDetailsResponse() {
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getTariffRate() {
        return tariffRate;
    }

    public void setTariffRate(String tariffRate) {
        this.tariffRate = tariffRate;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getOutstandingPaid() {
        return outstandingPaid;
    }

    public void setOutstandingPaid(String outstandingPaid) {
        this.outstandingPaid = outstandingPaid;
    }

    @Override
    public String toString() {
        return "VerifyPaymentDetailsResponse{" + "transactionID=" + transactionID + ", accountNumber=" + accountNumber + ", meterNumber=" + meterNumber + ", amount=" + amount + ", token=" + token + ", unit=" + units + ", tariffRate=" + tariffRate + ", vat=" + vat + ", outstandingPaid=" + outstandingPaid + '}';
    }

}
