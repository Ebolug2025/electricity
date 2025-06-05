package com.etranzact.vasgate.electricity.ekoedc.Domain.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@Data
@AllArgsConstructor
public class PaymentResponse {
    private long idVendor;
    private String codUser;
    private String meterSerial;
    private String account;
    private double debtPayment;
    private double totalPayment;
    private double accountBalance;
    private double unitsPayment;
    private double units;
    private String unitsType;
    private String paymentDate;
    private String receipt;
    private String customerName;
    private String tariffDescription;
    private List<UnitTopUp> unitsTopUp;
    private String comment;
    private List<String> listtoken;
    private List<String> mapTokens;
    private List<String> kctTokens;
    private long keyDataSGC;
    private long keyDataTI;
    private long keyDataKRN;
    private String requestID;
    private long channel;
    private double mapUnits;
    private double mapAmount;
    private double refundUnits;
    private double totalRefund;
    private long districtCode;
    private String districtName;




}
