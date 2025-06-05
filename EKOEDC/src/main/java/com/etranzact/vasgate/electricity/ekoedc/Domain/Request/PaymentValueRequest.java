package com.etranzact.vasgate.electricity.ekoedc.Domain.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class PaymentValueRequest {
    private String idVendor;
    private String codUser;
    private String meterSerial;
    private String account;
    private Double totalPayment;
    private Double debtPayment;
}
