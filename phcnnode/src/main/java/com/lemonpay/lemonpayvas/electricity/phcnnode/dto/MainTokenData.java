package com.lemonpay.lemonpayvas.electricity.phcnnode.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MainTokenData {
    private String unit;
    private String amount;
    private String vat;
    private String token;
    private String tokenTax;
    private String fixedCharge;
    private String keyDataSGC;
    private String keyDataTI;
    private String keyDataKRN;
    private String mapUnits;
    private String mapAmount;
    private String mapTokens;
    private String kctTokens;

}
