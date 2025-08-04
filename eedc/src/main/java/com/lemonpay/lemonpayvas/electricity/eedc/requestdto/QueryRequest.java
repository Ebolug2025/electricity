package com.lemonpay.lemonpayvas.electricity.eedc.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryRequest {

    private Long billersCode;
    private String serviceID;
    private String type;
    private String request_id;
}
