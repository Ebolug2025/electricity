package com.etranzact.vasgate.aedc.dto;

import com.etranzact.vasgate.aedc.model.UnitTopUp;

import java.util.List;

public class PaymentValueResponse {
    private String account;
    private String customerName;
    private String tariffDescription;
    private String serviceAddress;
    private long lastPaymentDate;
    private double amountLast;
    private double debtPayment;
    private double percentageDebt;
    private double accountBalance;
    private double unitsPayment;
    private double units;
    private String unitsType;
    private List<UnitTopUp> unitTopUp;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public long getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(long lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public double getAmountLast() {
        return amountLast;
    }

    public void setAmountLast(double amountLast) {
        this.amountLast = amountLast;
    }

    public double getDebtPayment() {
        return debtPayment;
    }

    public void setDebtPayment(double debtPayment) {
        this.debtPayment = debtPayment;
    }

    public double getPercentageDebt() {
        return percentageDebt;
    }

    public void setPercentageDebt(double percentageDebt) {
        this.percentageDebt = percentageDebt;
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

    public List<UnitTopUp> getUnitsTopUp() {
        return unitTopUp;
    }

    public void setUnitsTopUp(List<UnitTopUp> unitTopUp) {
        this.unitTopUp = unitTopUp;
    }

    @Override
    public String toString() {
        return "PaymentValueResponse{" +
                "account='" + account + '\'' +
                ", customerName='" + customerName + '\'' +
                ", tariffDescription='" + tariffDescription + '\'' +
                ", serviceAddress='" + serviceAddress + '\'' +
                ", lastPaymentDate=" + lastPaymentDate +
                ", amountLast=" + amountLast +
                ", debtPayment=" + debtPayment +
                ", percentageDebt=" + percentageDebt +
                ", accountBalance=" + accountBalance +
                ", unitsPayment=" + unitsPayment +
                ", units=" + units +
                ", unitsType='" + unitsType + '\'' +
                ", unitTopUp=" + unitTopUp +
                '}';
    }
}
