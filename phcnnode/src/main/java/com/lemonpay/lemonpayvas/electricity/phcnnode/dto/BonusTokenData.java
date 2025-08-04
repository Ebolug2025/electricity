package com.lemonpay.lemonpayvas.electricity.phcnnode.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BonusTokenData {
    private String unit;
    private String amount;
    private String vat;
    private String token;
    private String tokenTax;
    private String fixedCharge;


}
