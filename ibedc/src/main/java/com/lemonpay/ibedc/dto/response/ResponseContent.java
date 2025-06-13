package com.lemonpay.ibedc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseContent {
    private String Customer_Name;
    private String Address;
    private String Customer_Arrears;
    private String Min_Purchase_Amount;
    private String MeterNumber;
    private String Service_Band;
    private String Can_Vend;
    private String Customer_Account_Type;
    private String Meter_Type;
    private Boolean WrongBillersCode;
    private Commission_details commission_details;
}


