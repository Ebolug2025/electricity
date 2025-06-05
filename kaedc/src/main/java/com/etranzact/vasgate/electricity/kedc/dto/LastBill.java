package com.etranzact.vasgate.electricity.kedc.dto;

public class LastBill {

    private String bill_number;
    private String date;
    private double amount;

    public LastBill(String bill_number, String date, double amount) {
        this.bill_number = bill_number;
        this.date = date;
        this.amount = amount;
    }

    public String getBill_number() {
        return bill_number;
    }

    public void setBill_number(String bill_number) {
        this.bill_number = bill_number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
