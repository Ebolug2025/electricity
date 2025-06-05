package com.etranzact.vasgate.electricity.ekoedc.Domain.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class PaymentRequest {
    private String idVendor;
    private String codUser;
    private String meterSerial;
    private String account;
    private double totalPayment;
    private String requestID;
    private int channel;
}
