package com.lemonpay.lemonpayvas.electricity.eedc.responsedto;

import lombok.Data;

@Data
public class EnuguProcessResponse {

    private String code;
    private String response_description;
    private String requestId;
    private Double amount;
    private String transaction_date;
    private String purchased_code;
    private String exchangeReference;
    private String arrearsBalance;
    private String appliedToArrears;
    private String wallet;
    private Double vat;
    private String invoiceNumber;
    private String appliedToWallet;
    private Double units;
    private String token;
    private String kct1;
    private String kct2;
}
