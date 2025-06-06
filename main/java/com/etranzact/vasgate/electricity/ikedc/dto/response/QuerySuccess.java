package com.lemonpay.lemonpayvas.electricity.ikedc.dto.response;

import java.math.BigDecimal;

public class QuerySuccess {

    Boolean error;
    String discoCode;
    String vendType;
    String meterNo;
    BigDecimal minVendAmount;
    BigDecimal maxVendAmount;
    int responseCode;
    BigDecimal outstanding;
    BigDecimal debtRepayment;
    String name;
    String address;
    String tariff;
    String tariffDesc;
    String tariffClass;
    String orderId;

    public QuerySuccess() {
    }

    public QuerySuccess(Boolean error, String discoCode, String vendType, String meterNo, BigDecimal minVendAmount, BigDecimal maxVendAmount, int responseCode, BigDecimal outstanding, BigDecimal debtRepayment, String name, String address, String tariff, String tariffDesc, String tariffClass, String orderId) {
        this.error = error;
        this.discoCode = discoCode;
        this.vendType = vendType;
        this.meterNo = meterNo;
        this.minVendAmount = minVendAmount;
        this.maxVendAmount = maxVendAmount;
        this.responseCode = responseCode;
        this.outstanding = outstanding;
        this.debtRepayment = debtRepayment;
        this.name = name;
        this.address = address;
        this.tariff = tariff;
        this.tariffDesc = tariffDesc;
        this.tariffClass = tariffClass;
        this.orderId = orderId;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getDiscoCode() {
        return discoCode;
    }

    public void setDiscoCode(String discoCode) {
        this.discoCode = discoCode;
    }

    public String getVendType() {
        return vendType;
    }

    public void setVendType(String vendType) {
        this.vendType = vendType;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public BigDecimal getMinVendAmount() {
        return minVendAmount;
    }

    public void setMinVendAmount(BigDecimal minVendAmount) {
        this.minVendAmount = minVendAmount;
    }

    public BigDecimal getMaxVendAmount() {
        return maxVendAmount;
    }

    public void setMaxVendAmount(BigDecimal maxVendAmount) {
        this.maxVendAmount = maxVendAmount;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public BigDecimal getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(BigDecimal outstanding) {
        this.outstanding = outstanding;
    }

    public BigDecimal getDebtRepayment() {
        return debtRepayment;
    }

    public void setDebtRepayment(BigDecimal debtRepayment) {
        this.debtRepayment = debtRepayment;
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

    public String getTariff() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public String getTariffDesc() {
        return tariffDesc;
    }

    public void setTariffDesc(String tariffDesc) {
        this.tariffDesc = tariffDesc;
    }

    public String getTariffClass() {
        return tariffClass;
    }

    public void setTariffClass(String tariffClass) {
        this.tariffClass = tariffClass;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
