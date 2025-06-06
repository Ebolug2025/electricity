package com.lemonpay.lemonpayvas.electricity.ikedc.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityQueryResponse {

    private String code;
    private QueryContent content;
}
