package com.etranzact.vasgate.electricity.ekoedc.Domain.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CustEnqRequest {

    private String receipt;
    private String meterSerial;
    private String account;
    private String dateFrom;
    private String dateTo;
}
