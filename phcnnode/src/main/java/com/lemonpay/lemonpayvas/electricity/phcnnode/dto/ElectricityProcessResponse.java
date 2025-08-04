package com.lemonpay.lemonpayvas.electricity.phcnnode.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ElectricityProcessResponse {
    private String requestType;
    private String disco;
    private String accountType;
    private String uniqueTransId;
    private String accountNumber;
    private String amount;
    private MainTokenData mainToken;
    private BonusTokenData bonusToken;
    private List<MainTokenData> otherToken;
    private List<String> errors;
    private String externalReference;
    private String receiptNo;
    private String responseCode;
    private String errorCode;
    private String unitsPurchased;
    private String responseDesc;
    private String businessUnit;
    private String tax;
    private String tariff;
    private String tariffCode;
    private String feederName;
    private String feederBand;
    private String fault;
    private String customerArrears;
    private String tariffDescription;
    private String unitsPayment;
    private String balance;
    private String reconnectionFee;
    private String lossOfFee;
    private String penalty;
    private String serviceCharge;
    private String installationFee;
    private String adminCharge;
    private String productId;
    private String mobile;
    private String customerName;
    private String customerAddress;
    private String vendor;
    private String newAccountNumber;
    private String vat;
    private String deductions;
    private String payment_method;
    private String transaction_date;
    private String minmumPurchase;

}
