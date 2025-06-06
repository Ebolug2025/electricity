package com.lemonpay.lemonpayvas.electricity.ikedc.model;

public class Customer {
    private String account;
    private String accountType;
    private String meterSerial;
    private String accountBalance;
    private String name;
    private String serviceAddress;
    private String orgName;
    private String tariffDescription;
    private String feederName;
    private String outstandingDebt;

    public String getAccount() {
        return account;
    }

    public Customer setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getAccountType() {
        return accountType;
    }

    public Customer setAccountType(String accountType) {
        this.accountType = accountType;
        return this;
    }

    public String getMeterSerial() {
        return meterSerial;
    }

    public Customer setMeterSerial(String meterSerial) {
        this.meterSerial = meterSerial;
        return this;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public Customer setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
        return this;
    }

    public String getName() {
        return name;
    }

    public Customer setName(String name) {
        this.name = name;
        return this;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public Customer setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
        return this;
    }

    public String getOrgName() {
        return orgName;
    }

    public Customer setOrgName(String orgName) {
        this.orgName = orgName;
        return this;
    }

    public String getTariffDescription() {
        return tariffDescription;
    }

    public Customer setTariffDescription(String tariffDescription) {
        this.tariffDescription = tariffDescription;
        return this;
    }

    public String getFeederName() {
        return feederName;
    }

    public Customer setFeederName(String feederName) {
        this.feederName = feederName;
        return this;
    }

    public String getOutstandingDebt() {
        return outstandingDebt;
    }

    public Customer setOutstandingDebt(String outstandingDebt) {
        this.outstandingDebt = outstandingDebt;
        return this;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "account='" + account + '\'' +
                ", accountType='" + accountType + '\'' +
                ", meterSerial='" + meterSerial + '\'' +
                ", accountBalance='" + accountBalance + '\'' +
                ", name='" + name + '\'' +
                ", serviceAddress='" + serviceAddress + '\'' +
                ", orgName='" + orgName + '\'' +
                ", tariffDescription='" + tariffDescription + '\'' +
                ", feederName='" + feederName + '\'' +
                ", outstandingDebt='" + outstandingDebt + '\'' +
                '}';
    }
}
