package com.lemonpay.lemonpayvas.electricity.phcnnode.dto;

import lombok.Data;

@Data
public class ElectricityQueryRequest {
    private String reference;
    private String type;
    private String channel;
    private double amount;
    private String payerId;
    private String mobile;
}
