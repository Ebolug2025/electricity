package com.lemonpay.ibedc.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private String serviceId;
    private String serviceID;
    private String meterNumber;
    private String meterType;
    private Double amount;
    private String mobile;

}
