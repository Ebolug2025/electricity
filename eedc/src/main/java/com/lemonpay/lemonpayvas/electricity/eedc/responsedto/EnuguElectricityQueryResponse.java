package com.lemonpay.lemonpayvas.electricity.eedc.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnuguElectricityQueryResponse {

    private String code;
    private String response_description;
    private QueryContent content;
}
