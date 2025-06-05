package com.etranzact.vasgate.aedc.dto;

public class PaymentRequest {

    String idVendor;
    String codUser;
    String meterSerial;
    double totalPayment;
    double debtPayment;
    String account;
    String tariffDescription;
    double percentageDebt;
    double accountBalance;
    double unitsPayment;
    double units;
    String unitsType;
    String comment;
    String requestID;
    int channel;
    int areaCode;
    int serviceCode;
    String phoneNo;
    String email;

    public String getIdVendor() {
        return idVendor;
    }
    public PaymentRequest setIdVendor(String idVendor) {
        this.idVendor = idVendor;
        return this;
    }
    public String getCodUser() {
        return codUser;
    }
    public PaymentRequest setCodUser(String codUser) {
        this.codUser = codUser;
        return this;
    }
    public String getMeterSerial() {
        return meterSerial;
    }
    public PaymentRequest setMeterSerial(String meterSerial) {
        this.meterSerial = meterSerial;
        return this;
    }
    public double getTotalPayment() {
        return totalPayment;
    }
    public PaymentRequest setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
        return this;
    }
    public double getDebtPayment() {
        return debtPayment;
    }
    public PaymentRequest setDebtPayment(double debtPayment) {
        this.debtPayment = debtPayment;
        return this;
    }
    public String getAccount() {
        return account;
    }
    public PaymentRequest setAccount(String account) {
        this.account = account;
        return this;
    }
    public String getTariffDescription() {
        return tariffDescription;
    }
    public PaymentRequest setTariffDescription(String tariffDescription) {
        this.tariffDescription = tariffDescription;
        return this;
    }
    public double getPercentageDebt() {
        return percentageDebt;
    }
    public PaymentRequest setPercentageDebt(double percentageDebt) {
        this.percentageDebt = percentageDebt;
        return this;
    }
    public double getAccountBalance() {
        return accountBalance;
    }
    public PaymentRequest setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
        return this;
    }
    public double getUnitsPayment() {
        return unitsPayment;
    }
    public PaymentRequest setUnitsPayment(double unitsPayment) {
        this.unitsPayment = unitsPayment;
        return this;
    }
    public double getUnits() {
        return units;
    }
    public PaymentRequest setUnits(double units) {
        this.units = units;
        return this;
    }
    public String getUnitsType() {
        return unitsType;
    }
    public PaymentRequest setUnitsType(String unitsType) {
        this.unitsType = unitsType;
        return this;
    }
    public String getComment() {
        return comment;
    }
    public PaymentRequest setComment(String comment) {
        this.comment = comment;
        return this;
    }
    public String getRequestID() {
        return requestID;
    }
    public PaymentRequest setRequestID(String requestID) {
        this.requestID = requestID;
        return this;
    }
    public int getChannel() {
        return channel;
    }
    public PaymentRequest setChannel(int channel) {
        this.channel = channel;
        return this;
    }
    public int getAreaCode() {
        return areaCode;
    }
    public PaymentRequest setAreaCode(int areaCode) {
        this.areaCode = areaCode;
        return this;
    }
    public int getServiceCode() {
        return serviceCode;
    }
    public PaymentRequest setServiceCode(int serviceCode) {
        this.serviceCode = serviceCode;
        return this;
    }
    public String getPhoneNo() {
        return phoneNo;
    }
    public PaymentRequest setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
        return this;
    }
    public String getEmail() {
        return email;
    }
    public PaymentRequest setEmail(String email) {
        this.email = email;
        return this;
    }
    @Override
    public String toString() {
        return "Payment [idVendor=" + idVendor + ", codUser=" + codUser + ", meterSerial=" + meterSerial
                + ", totalPayment=" + totalPayment + ", debtPayment=" + debtPayment + ", account=" + account
                + ", tariffDescription=" + tariffDescription + ", percentageDebt=" + percentageDebt
                + ", accountBalance=" + accountBalance + ", unitsPayment=" + unitsPayment + ", units=" + units
                + ", unitsType=" + unitsType + ", comment=" + comment + ", requestID=" + requestID + ", channel="
                + channel + ", areaCode=" + areaCode + ", serviceCode=" + serviceCode + ", phoneNo=" + phoneNo
                + ", email=" + email + "]";
    }
}
