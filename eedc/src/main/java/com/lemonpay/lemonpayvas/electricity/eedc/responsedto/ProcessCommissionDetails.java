package com.lemonpay.lemonpayvas.electricity.eedc.responsedto;

import lombok.Data;

@Data
public class ProcessCommissionDetails {

    private Double amount;
    private String rate_type;
    private String rate;
    private String computation_type;
}
