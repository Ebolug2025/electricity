package com.etranzact.vasgate.electricity.kedc.dto;

public class VerificationRequest {
    private String meter_number;
    private String account_number;

    public VerificationRequest() {
        this.meter_number = meter_number;
        this.account_number = account_number;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getMeter_number() {
        return meter_number;
    }

    public void setMeter_number(String meter_number) {
        this.meter_number = meter_number;
    }

    @Override
    public String toString() {
        return "VerificationRequest{" +
                "meter_number='" + meter_number + '\'' +
                ", account_number='" + account_number + '\'' +
                '}';
    }
}
