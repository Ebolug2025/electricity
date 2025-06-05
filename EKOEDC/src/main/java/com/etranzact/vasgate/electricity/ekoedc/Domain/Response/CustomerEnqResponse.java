package com.etranzact.vasgate.electricity.ekoedc.Domain.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CustomerEnqResponse {


    private String idVendor;
    private String descVendor;
    private String nameCashier;
    private String codUser;
    private String dateTransaction;
    private String receipt;
    private String customerName;
    private String account;
    private String meterSerial;
    private double debtPayment;
    private double unitsPayment;
    private double totalAmount;
    private double units;
    private String untisType;
    private String transactionId;
    private String requestID;
}
