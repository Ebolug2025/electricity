package com.etranzact.vasgate.aedc.dto;

import com.etranzact.vasgate.aedc.model.UnitTopUp;

import java.util.List;

public class PaymentResponse
{
    private long idVendor;
    private String codUser;
    private String meterSerial;
    private String account;
    private double debtPayment;
    private double totalPayment;
    private double accountBalance;
    private double unitsPayment;
    private double units;
    private String unitsType;
    private String paymentDate;
    private String receipt;
    private String customerName;
    private String tariffDescription;
    private List<UnitTopUp> unitsTopUp;
    private String comment;
    private List<String> listtoken;
    private long keyDataSGC;
    private long keyDataTI;
    private long keyDataKRN;
    private String requestID;
    private long channel;

    public long getIdVendor() {
        return idVendor;
    }

    public void setIdVendor(long idVendor) {
        this.idVendor = idVendor;
    }

    public String getCodUser() {
        return codUser;
    }

    public void setCodUser(String codUser) {
        this.codUser = codUser;
    }

    public String getMeterSerial() {
        return meterSerial;
    }

    public void setMeterSerial(String meterSerial) {
        this.meterSerial = meterSerial;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public double getDebtPayment() {
        return debtPayment;
    }

    public void setDebtPayment(double debtPayment) {
        this.debtPayment = debtPayment;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getUnitsPayment() {
        return unitsPayment;
    }

    public void setUnitsPayment(double unitsPayment) {
        this.unitsPayment = unitsPayment;
    }

    public double getUnits() {
        return units;
    }

    public void setUnits(double units) {
        this.units = units;
    }

    public String getUnitsType() {
        return unitsType;
    }

    public void setUnitsType(String unitsType) {
        this.unitsType = unitsType;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTariffDescription() {
        return tariffDescription;
    }

    public void setTariffDescription(String tariffDescription) {
        this.tariffDescription = tariffDescription;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getListtoken() {
        return listtoken;
    }

    public void setListtoken(List<String> listtoken) {
        this.listtoken = listtoken;
    }

    public long getKeyDataSGC() {
        return keyDataSGC;
    }

    public void setKeyDataSGC(long keyDataSGC) {
        this.keyDataSGC = keyDataSGC;
    }

    public long getKeyDataTI() {
        return keyDataTI;
    }

    public void setKeyDataTI(long keyDataTI) {
        this.keyDataTI = keyDataTI;
    }

    public long getKeyDataKRN() {
        return keyDataKRN;
    }

    public void setKeyDataKRN(long keyDataKRN) {
        this.keyDataKRN = keyDataKRN;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public long getChannel() {
        return channel;
    }

    public void setChannel(long channel) {
        this.channel = channel;
    }

    public List<UnitTopUp> getUnitsTopUp() {
        return unitsTopUp;
    }

    public void setUnitsTopUp(List<UnitTopUp> unitsTopUp) {
        this.unitsTopUp = unitsTopUp;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    @Override
    public String toString() {
        return "PaymentResponse{" +
                "idVendor=" + idVendor +
                ", codUser='" + codUser + '\'' +
                ", meterSerial='" + meterSerial + '\'' +
                ", account='" + account + '\'' +
                ", debtPayment=" + debtPayment +
                ", totalPayment=" + totalPayment +
                ", accountBalance=" + accountBalance +
                ", unitsPayment=" + unitsPayment +
                ", units=" + units +
                ", unitsType='" + unitsType + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                ", receipt='" + receipt + '\'' +
                ", customerName='" + customerName + '\'' +
                ", tariffDescription='" + tariffDescription + '\'' +
                ", unitsTopUp=" + unitsTopUp +
                ", comment='" + comment + '\'' +
                ", listtoken=" + listtoken +
                ", keyDataSGC=" + keyDataSGC +
                ", keyDataTI=" + keyDataTI +
                ", keyDataKRN=" + keyDataKRN +
                ", requestID='" + requestID + '\'' +
                ", channel=" + channel +
                '}';
    }
}
