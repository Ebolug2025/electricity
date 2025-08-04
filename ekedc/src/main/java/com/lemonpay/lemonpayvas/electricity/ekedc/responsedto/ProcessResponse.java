package com.lemonpay.lemonpayvas.electricity.ekedc.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessResponse {

    private String code;
    private ProcessContent content;
    private String response_description;
    private String requestId;
    private Double amount;
    private String transaction_date;
    private String purchased_code;
    private String exchangeReference;
    private String arrearsBalance;
    private String appliedToArrears;
    private String mainToken;
    private String mainTokenDescription;
    private String mainTokenUnits;
    private Double mainTokenTax;
    private Double mainsTokenAmount;
    private String bonusToken;
    private String bonusTokenUnits;
    private String bonusTokenDescription;
    private Double bonusTokenTax;
    private Double bonusTokenAmount;
    private Double debtAmount;
    private String wallet;
    private Double vat;
    private Double debtTariff;
    private String invoiceNumber;
    private String appliedToWallet;
    private Double units;
    private String token;
    private String kct1;
    private String kct2;

}
