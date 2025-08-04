package com.lemonpay.lemonpayvas.electricity.ekedc.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EkoElectricityQueryResponse {

    private String code;
    private String response_description;
    private QueryContent content;
}
