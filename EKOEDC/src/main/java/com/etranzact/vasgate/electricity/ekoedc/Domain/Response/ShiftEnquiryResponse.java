package com.etranzact.vasgate.electricity.ekoedc.Domain.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ShiftEnquiryResponse {


    private String idVendor;
    private String codUser;
    private String receipt;
    private String customerName;
    private String account;
    private String meterSerial;
    private double debtPayment;
    private double unitPayment;
    private double totalAmount;
    private double unit;
    private String unitstype;
    private Long transactionDate;
    private String requestID;
}
