package com.etranzact.vasgate.electricity.ekoedc.Domain.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CustomerEnqRequest {

    private String idVendor;
    private String codUser;
    private String receipt;
    private String meterSerial;
    private String account;
    private Long dateFrom;
    private Long dateTo;
}
