/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.vasgate.electricity.jedc.dto;

/**
 *
 * @author cindy.eke
 */
public class Customer 
{
    private String name;
    private String address;
    private String meterNumber;
    private String customerType;
    private String phone;
    private String feeder_33_11_dt;
    private String tariff;
    private String tariffRate;
    private String outStanding;
    private String accountNumber;

    public Customer(String name, String address, String meterNumber, String customerType, String phone, String feeder_33_11_dt, String tariff, String tariffRate, String outStanding, String accountNumber) {
        this.name = name;
        this.address = address;
        this.meterNumber = meterNumber;
        this.customerType = customerType;
        this.phone = phone;
        this.feeder_33_11_dt = feeder_33_11_dt;
        this.tariff = tariff;
        this.tariffRate = tariffRate;
        this.outStanding = outStanding;
        this.accountNumber = accountNumber;
    }

    public Customer() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFeeder_33_11_dt() {
        return feeder_33_11_dt;
    }

    public void setFeeder_33_11_dt(String feeder_33_11_dt) {
        this.feeder_33_11_dt = feeder_33_11_dt;
    }

    public String getTariff() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public String getTariffRate() {
        return tariffRate;
    }

    public void setTariffRate(String tariffRate) {
        this.tariffRate = tariffRate;
    }

    public String getOutStanding() {
        return outStanding;
    }

    public void setOutStanding(String outStanding) {
        this.outStanding = outStanding;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return "Customer{" + "name=" + name + ", address=" + address + ", meterNumber=" + meterNumber + ", customerType=" + customerType + ", phone=" + phone + ", feeder_33_11_dt=" + feeder_33_11_dt + ", tariff=" + tariff + ", tariffRate=" + tariffRate + ", outStanding=" + outStanding + '}';
    }

    
}
