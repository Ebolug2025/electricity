package com.etranzact.vasgate.aedc.dto;

public class CustomerRequest
{
    private String idVendor;
    private String codUser;
    private String codType;
    private String value;

    public CustomerRequest() {}

    public CustomerRequest(String idVendor, String codUser, String codType, String meterNumber) {
        this.idVendor = idVendor;
        this.codUser = codUser;
        this.codType = codType;
        this.value = meterNumber;
    }

    public String getIdVendor() {
        return idVendor;
    }

    public CustomerRequest setIdVendor(String idVendor) {
        this.idVendor = idVendor;
        return this;
    }

    public String getCodUser() {
        return codUser;
    }

    public CustomerRequest setCodUser(String codUser) {
        this.codUser = codUser;
        return this;
    }

    public String getCodType() {
        return codType;
    }

    public CustomerRequest setCodType(String codType) {
        this.codType = codType;
        return this;
    }

    public String getValue() {
        return value;
    }

    public CustomerRequest setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "CustomerRequest{" +
                "idVendor='" + idVendor + '\'' +
                ", codUser='" + codUser + '\'' +
                ", codType='" + codType + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
