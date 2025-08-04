package com.lemonpay.lemonpayvas.electricity.ekedc.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessRequestdto {

    private String request_id;
    private String serviceID;
    private String billersCode;
    private String variation_code;
    private Double amount;
    private Long phone;
}
