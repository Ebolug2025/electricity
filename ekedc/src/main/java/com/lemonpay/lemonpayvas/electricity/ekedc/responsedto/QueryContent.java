package com.lemonpay.lemonpayvas.electricity.ekedc.responsedto;

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
    private String Account_Number;
    private String Customer_District;
    private String Business_Unit;
    private String Minimum_Amount;
    private String Min_Purchase_Amount;
    private String Tariff;
    private CommissionDetails commission_details;
}
