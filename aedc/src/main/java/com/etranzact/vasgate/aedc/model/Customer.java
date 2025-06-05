package com.etranzact.vasgate.aedc.model;

public class Customer {
    private String meterSerial;
    private String account;
    private String accountBalance;
    private String tariffDescription;
    private String indicatorPrePostAccount;
    private String serviceAddress;
    private String name;

    public String getMeterSerial() {
        return meterSerial;
    }

    public Customer setMeterSerial(String meterSerial) {
        this.meterSerial = meterSerial;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public Customer setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public Customer setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
        return this;
    }

    public String getTariffDescription() {
        return tariffDescription;
    }

    public Customer setTariffDescription(String tariffDescription) {
        this.tariffDescription = tariffDescription;
        return this;
    }

    public String getIndicatorPrePostAccount() {
        return indicatorPrePostAccount;
    }

    public Customer setIndicatorPrePostAccount(String indicatorPrePostAccount) {
        this.indicatorPrePostAccount = indicatorPrePostAccount;
        return this;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public Customer setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
        return this;
    }

    public String getName() {
        return name;
    }

    public Customer setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "meterSerial='" + meterSerial + '\'' +
                ", account='" + account + '\'' +
                ", accountBalance='" + accountBalance + '\'' +
                ", tariffDescription='" + tariffDescription + '\'' +
                ", indicatorPrePostAccount='" + indicatorPrePostAccount + '\'' +
                ", serviceAddress='" + serviceAddress + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
