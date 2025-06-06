package com.lemonpay.lemonpayvas.electricity.ikedc.dto.response;

import java.util.List;

public class VendDataDto {

    private Long id;
    private String amountGenerated;
    private String tariff;
    private String debtAmount;
    private String debtRemaining;
    private String disco;
    private String freeUnits;
    private String orderId;
    private String receiptNo;
    private String tax;
    private String vendTime;
    private String token;
    private String totalAmountPaid;
    private String units;
    private String vendAmount;
    private String vendRef;
    private int responseCode;
    private String responseMessage;
    private String phoneNo;
    private String address;

    private String name;

    private List<VendDataParcel> parcels;


    public VendDataDto() {
    }

    public VendDataDto(Long id, String amountGenerated, String tariff, String debtAmount, String debtRemaining, String disco, String freeUnits, String orderId, String receiptNo, String tax, String vendTime, String token, String totalAmountPaid, String units, String vendAmount, String vendRef, int responseCode, String responseMessage, String phoneNo, String address, String name, List<VendDataParcel> parcels) {
        this.id = id;
        this.amountGenerated = amountGenerated;
        this.tariff = tariff;
        this.debtAmount = debtAmount;
        this.debtRemaining = debtRemaining;
        this.disco = disco;
        this.freeUnits = freeUnits;
        this.orderId = orderId;
        this.receiptNo = receiptNo;
        this.tax = tax;
        this.vendTime = vendTime;
        this.token = token;
        this.totalAmountPaid = totalAmountPaid;
        this.units = units;
        this.vendAmount = vendAmount;
        this.vendRef = vendRef;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.phoneNo = phoneNo;
        this.address = address;
        this.name = name;
        this.parcels = parcels;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAmountGenerated() {
        return amountGenerated;
    }

    public void setAmountGenerated(String amountGenerated) {
        this.amountGenerated = amountGenerated;
    }

    public String getTariff() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public String getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(String debtAmount) {
        this.debtAmount = debtAmount;
    }

    public String getDebtRemaining() {
        return debtRemaining;
    }

    public void setDebtRemaining(String debtRemaining) {
        this.debtRemaining = debtRemaining;
    }

    public String getDisco() {
        return disco;
    }

    public void setDisco(String disco) {
        this.disco = disco;
    }

    public String getFreeUnits() {
        return freeUnits;
    }

    public void setFreeUnits(String freeUnits) {
        this.freeUnits = freeUnits;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getVendTime() {
        return vendTime;
    }

    public void setVendTime(String vendTime) {
        this.vendTime = vendTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(String totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getVendAmount() {
        return vendAmount;
    }

    public void setVendAmount(String vendAmount) {
        this.vendAmount = vendAmount;
    }

    public String getVendRef() {
        return vendRef;
    }

    public void setVendRef(String vendRef) {
        this.vendRef = vendRef;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VendDataParcel> getParcels() {
        return parcels;
    }

    public void setParcels(List<VendDataParcel> parcels) {
        this.parcels = parcels;
    }
}