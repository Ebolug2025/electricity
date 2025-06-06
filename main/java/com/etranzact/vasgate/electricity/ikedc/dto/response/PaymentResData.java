/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lemonpay.lemonpayvas.electricity.ikedc.dto.response;

/**
 *
 * @author ugochukwu.omeje
 */
public class PaymentResData {
    private String amountTendered;
    private boolean success;
    private String message;
    private String totalUnits;
    private String dtName;
    private String rate;
    private String presentUnits;
    private String token;
    private String refund;
    private String name;
    private String balance;
    private String serviceAddress;
    private String orgNO;
    private String meterSerialNO; 
    private String orgName;
    private String transactionDate;
    private String remainingDebt;
    private String vatRate;
    private String receiptNO;
    private String units;
    private String adjustUnits;
    private String accountType;
    private String tariffName;
    private Object kct;
    private String feederName;

    private PaymentResList list;
    private String ref;


    public PaymentResData(String amountTendered, boolean success, String message, String totalUnits, String dtName, String rate, String presentUnits, String token, String refund, String name, String balance, String serviceAddress, String orgNO, String meterSerialNO, String orgName, String transactionDate, String remainingDebt, String vatRate, String receiptNO, String units, String adjustUnits, String accountType, String tariffName, Object kct, String feederName, PaymentResList list, String ref) {
        this.amountTendered = amountTendered;
        this.success = success;
        this.message = message;
        this.totalUnits = totalUnits;
        this.dtName = dtName;
        this.rate = rate;
        this.presentUnits = presentUnits;
        this.token = token;
        this.refund = refund;
        this.name = name;
        this.balance = balance;
        this.serviceAddress = serviceAddress;
        this.orgNO = orgNO;
        this.meterSerialNO = meterSerialNO;
        this.orgName = orgName;
        this.transactionDate = transactionDate;
        this.remainingDebt = remainingDebt;
        this.vatRate = vatRate;
        this.receiptNO = receiptNO;
        this.units = units;
        this.adjustUnits = adjustUnits;
        this.accountType = accountType;
        this.tariffName = tariffName;
        this.kct = kct;
        this.feederName = feederName;
        this.list = list;
        this.ref = ref;
    }

    public PaymentResData() {
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getOrgNO() {
        return orgNO;
    }

    public void setOrgNO(String orgNO) {
        this.orgNO = orgNO;
    }

    public String getMeterSerialNO() {
        return meterSerialNO;
    }

    public void setMeterSerialNO(String meterSerialNO) {
        this.meterSerialNO = meterSerialNO;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getVatRate() {
        return vatRate;
    }

    public void setVatRate(String vatRate) {
        this.vatRate = vatRate;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getAdjustUnits() {
        return adjustUnits;
    }

    public void setAdjustUnits(String adjustUnits) {
        this.adjustUnits = adjustUnits;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }

    public Object getKct() {
        return kct;
    }

    public void setKct(Object kct) {
        this.kct = kct;
    }

    public String getFeederName() {
        return feederName;
    }

    public void setFeederName(String feederName) {
        this.feederName = feederName;
    }

    public PaymentResList getList() {
        return list;
    }

    public void setList(PaymentResList list) {
        this.list = list;
    }

    public String getAmountTendered() {
        return amountTendered;
    }

    public void setAmountTendered(String amountTendered) {
        this.amountTendered = amountTendered;
    }

    public String getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(String totalUnits) {
        this.totalUnits = totalUnits;
    }

    public String getDtName() {
        return dtName;
    }

    public void setDtName(String dtName) {
        this.dtName = dtName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPresentUnits() {
        return presentUnits;
    }

    public void setPresentUnits(String presentUnits) {
        this.presentUnits = presentUnits;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiptNO() {
        return receiptNO;
    }

    public void setReceiptNO(String receiptNO) {
        this.receiptNO = receiptNO;
    }

    public void setRemainingDebt(String remainingDebt) {
        this.remainingDebt = remainingDebt;
    }

    public String getRemaininDept() {
        return remainingDebt;
    }

   
    
    
}
