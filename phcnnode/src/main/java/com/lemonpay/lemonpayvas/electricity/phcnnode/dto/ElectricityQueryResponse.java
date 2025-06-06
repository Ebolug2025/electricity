package com.lemonpay.lemonpayvas.electricity.phcnnode.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ElectricityQueryResponse {
    private String requestType;
    private String disco;
    private String customerType;
    private String accountNumber;
    private String uniqueTransId;
    private String externalReference;
    private String gender;
    private String errorCode;
    private String responseCode;
    private String responseDesc;
    private String customerName;
    private String customerAddress;
    private String businessUnit;
    private String state;
    private String email;
    private String customerArrears;
    private String minimumPurchase;
    private String maxPurchase;
    private String debtRepayment;
    private String merchantCode;
    private String tariff;
    private String tariffDesc;
    private String tariffClass;
    private String orderId;
    private String unitsPayment;
    private String units;
    private String name;
    private String tariffCode;
    private String mobile;
    private String accountStatus;
    private String vat;
    private double deductions;
}
