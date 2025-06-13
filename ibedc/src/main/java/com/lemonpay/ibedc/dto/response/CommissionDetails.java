package com.lemonpay.ibedc.dto.response;

import lombok.Data;

@Data
public class CommissionDetails {
    private int amount;
    private String rate;
    private String rate_type;
    private String computation_type;
}
