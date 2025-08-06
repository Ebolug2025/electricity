package com.lemonpay.lemonpayvas.electricity.eedc.responsedto;

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
    private String District;
    private String Min_Purchase_Amount;
    private String Tariff;
    private String error;
    private CommissionDetails commission_details;
}
