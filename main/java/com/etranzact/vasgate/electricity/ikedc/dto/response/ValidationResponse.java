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
public class ValidationResponse {
    
    private String accountNO;
    private String accountType;
    private String meterSerialNO;
    private String minimumVend;
    private String mobilePhone;
    private String name;
    private String outstandingDebt;
    private String serviceAddress;
    private String feederName;
    private String tariffName;
    private String tariff;

    public ValidationResponse(String accountNO, String accountType, String meterSerialNO, String minimumVend, String mobilePhone, String name, String outstandingDebt, String serviceAddress, String feederName, String tariffName, String tariff) {
        this.accountNO = accountNO;
        this.accountType = accountType;
        this.meterSerialNO = meterSerialNO;
        this.minimumVend = minimumVend;
        this.mobilePhone = mobilePhone;
        this.name = name;
        this.outstandingDebt = outstandingDebt;
        this.serviceAddress = serviceAddress;
        this.feederName = feederName;
        this.tariffName = tariffName;
        this.tariff = tariff;
    }


    public ValidationResponse() {
    }

    public String getAccountNO() {
        return accountNO;
    }

    public void setAccountNO(String accountNO) {
        this.accountNO = accountNO;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getMeterSerialNO() {
        return meterSerialNO;
    }

    public void setMeterSerialNO(String meterSerialNO) {
        this.meterSerialNO = meterSerialNO;
    }

    public String getMinimumVend() {
        return minimumVend;
    }

    public void setMinimumVend(String minimumVend) {
        this.minimumVend = minimumVend;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutstandingDebt() {
        return outstandingDebt;
    }

    public void setOutstandingDebt(String outstandingDebt) {
        this.outstandingDebt = outstandingDebt;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getFeederName() {
        return feederName;
    }

    public void setFeederName(String feederName) {
        this.feederName = feederName;
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }

    public String getTariff() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    @Override
    public String toString() {
        return "ValidationResponse{" +
                "accountNO='" + accountNO + '\'' +
                ", accountType='" + accountType + '\'' +
                ", meterSerialNO='" + meterSerialNO + '\'' +
                ", minimumVend='" + minimumVend + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", name='" + name + '\'' +
                ", outstandingDebt='" + outstandingDebt + '\'' +
                ", serviceAddress='" + serviceAddress + '\'' +
                ", feederName='" + feederName + '\'' +
                ", tariffName='" + tariffName + '\'' +
                ", tariff='" + tariff + '\'' +
                '}';
    }
}
