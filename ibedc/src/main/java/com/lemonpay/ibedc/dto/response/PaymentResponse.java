package com.lemonpay.ibedc.dto.response;

import lombok.Data;

@Data
public class PaymentResponse {
    private String code;
    private PaymentContent content;
    private String response_description;
    private String requestId;
    private Double amount;
    private String transaction_date;
    private String purchased_code;
    private String CustomerName;
    private String CustomerAddress;
    private String ReceiptNumber;
    private int Amount;
    private String Tax;
    private String Units;
    private String Token;
    private String Tariff;
    private String Description;
    private String KCT1;
    private String KCT2;
}
