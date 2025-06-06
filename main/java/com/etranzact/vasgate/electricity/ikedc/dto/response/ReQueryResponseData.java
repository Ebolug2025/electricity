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
public class ReQueryResponseData {
    
    private String id;
    private String ref;
    private Double amountVend;
    private String requestNumber;
    private String transactionStatus;
    private boolean paymentStatus;
    private boolean accountNO; //
    private String accountName; //
    private String meterNO;
    private String address;
    private String accountType; //
    private String remainingDept;
    private String product;
    private String token;
    private String[] kct;
    private String transactionDat;
    private String accountTypeCIS;
    private String minimumVend;
    private String kind;
    private String unitsPurchased;
    private String units;
    private String costOfUnits;
    private String debt;
    private String TransactionError;
    private String receiptNO;
    private String vatRate;
    private String vat;
    private String balance;
    private String rate;
    private String tariffName;
    private String LossofRevenue;
    private String ReconnectionFee;
    private String Penalty;
    private String ServiceCharge;
    private String InstallationFee;
    private String ReplacementCost;
    private String AdministrativeCharge;
    private String valueDate;
    private String updatedAt;

    public ReQueryResponseData(String id, String ref, Double amountVend, String requestNumber, String transactionStatus, boolean paymentStatus, boolean accountNO, String accountName, String meterNO, String address, String accountType, String remainingDept, String product, String token, String[] kct, String transactionDat, String accountTypeCIS, String minimumVend, String kind, String unitsPurchased, String units, String costOfUnits, String debt, String TransactionError, String receiptNO, String vatRate, String vat, String balance, String rate, String tariffName, String LossofRevenue, String ReconnectionFee, String Penalty, String ServiceCharge, String InstallationFee, String ReplacementCost, String AdministrativeCharge, String valueDate, String updatedAt) {
        this.id = id;
        this.ref = ref;
        this.amountVend = amountVend;
        this.requestNumber = requestNumber;
        this.transactionStatus = transactionStatus;
        this.paymentStatus = paymentStatus;
        this.accountNO = accountNO;
        this.accountName = accountName;
        this.meterNO = meterNO;
        this.address = address;
        this.accountType = accountType;
        this.remainingDept = remainingDept;
        this.product = product;
        this.token = token;
        this.kct = kct;
        this.transactionDat = transactionDat;
        this.accountTypeCIS = accountTypeCIS;
        this.minimumVend = minimumVend;
        this.kind = kind;
        this.unitsPurchased = unitsPurchased;
        this.units = units;
        this.costOfUnits = costOfUnits;
        this.debt = debt;
        this.TransactionError = TransactionError;
        this.receiptNO = receiptNO;
        this.vatRate = vatRate;
        this.vat = vat;
        this.balance = balance;
        this.rate = rate;
        this.tariffName = tariffName;
        this.LossofRevenue = LossofRevenue;
        this.ReconnectionFee = ReconnectionFee;
        this.Penalty = Penalty;
        this.ServiceCharge = ServiceCharge;
        this.InstallationFee = InstallationFee;
        this.ReplacementCost = ReplacementCost;
        this.AdministrativeCharge = AdministrativeCharge;
        this.valueDate = valueDate;
        this.updatedAt = updatedAt;
    }

    public ReQueryResponseData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Double getAmountVend() {
        return amountVend;
    }

    public void setAmountVend(Double amountVend) {
        this.amountVend = amountVend;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isAccountNO() {
        return accountNO;
    }

    public void setAccountNO(boolean accountNO) {
        this.accountNO = accountNO;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getMeterNO() {
        return meterNO;
    }

    public void setMeterNO(String meterNO) {
        this.meterNO = meterNO;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getRemainingDept() {
        return remainingDept;
    }

    public void setRemainingDept(String remainingDept) {
        this.remainingDept = remainingDept;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String[] getKct() {
        return kct;
    }

    public void setKct(String[] kct) {
        this.kct = kct;
    }

    public String getTransactionDat() {
        return transactionDat;
    }

    public void setTransactionDat(String transactionDat) {
        this.transactionDat = transactionDat;
    }

    public String getAccountTypeCIS() {
        return accountTypeCIS;
    }

    public void setAccountTypeCIS(String accountTypeCIS) {
        this.accountTypeCIS = accountTypeCIS;
    }

    public String getMinimumVend() {
        return minimumVend;
    }

    public void setMinimumVend(String minimumVend) {
        this.minimumVend = minimumVend;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getUnitsPurchased() {
        return unitsPurchased;
    }

    public void setUnitsPurchased(String unitsPurchased) {
        this.unitsPurchased = unitsPurchased;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getCostOfUnits() {
        return costOfUnits;
    }

    public void setCostOfUnits(String costOfUnits) {
        this.costOfUnits = costOfUnits;
    }

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public String getTransactionError() {
        return TransactionError;
    }

    public void setTransactionError(String TransactionError) {
        this.TransactionError = TransactionError;
    }

    public String getReceiptNO() {
        return receiptNO;
    }

    public void setReceiptNO(String receiptNO) {
        this.receiptNO = receiptNO;
    }

    public String getVatRate() {
        return vatRate;
    }

    public void setVatRate(String vatRate) {
        this.vatRate = vatRate;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }

    public String getLossofRevenue() {
        return LossofRevenue;
    }

    public void setLossofRevenue(String LossofRevenue) {
        this.LossofRevenue = LossofRevenue;
    }

    public String getReconnectionFee() {
        return ReconnectionFee;
    }

    public void setReconnectionFee(String ReconnectionFee) {
        this.ReconnectionFee = ReconnectionFee;
    }

    public String getPenalty() {
        return Penalty;
    }

    public void setPenalty(String Penalty) {
        this.Penalty = Penalty;
    }

    public String getServiceCharge() {
        return ServiceCharge;
    }

    public void setServiceCharge(String ServiceCharge) {
        this.ServiceCharge = ServiceCharge;
    }

    public String getInstallationFee() {
        return InstallationFee;
    }

    public void setInstallationFee(String InstallationFee) {
        this.InstallationFee = InstallationFee;
    }

    public String getReplacementCost() {
        return ReplacementCost;
    }

    public void setReplacementCost(String ReplacementCost) {
        this.ReplacementCost = ReplacementCost;
    }

    public String getAdministrativeCharge() {
        return AdministrativeCharge;
    }

    public void setAdministrativeCharge(String AdministrativeCharge) {
        this.AdministrativeCharge = AdministrativeCharge;
    }

    public String getValueDate() {
        return valueDate;
    }

    public void setValueDate(String valueDate) {
        this.valueDate = valueDate;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
     
    
}
