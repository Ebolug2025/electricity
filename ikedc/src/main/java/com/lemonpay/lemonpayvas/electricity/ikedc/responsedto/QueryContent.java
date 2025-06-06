package com.lemonpay.lemonpayvas.electricity.ikedc.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryContent {

    private String Customer_Name;
    private String Address;
    private String Meter_Number;
    private String Minimum_Amount;
    private String Min_Purchase_Amount;
    private String Can_Vend;
    private String Business_Unit;
    private String Customer_Account_Type;
    private String Meter_Type;
    private String WrongBillersCode;
}
