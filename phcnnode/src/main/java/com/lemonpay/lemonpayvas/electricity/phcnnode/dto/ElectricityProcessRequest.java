package com.lemonpay.lemonpayvas.electricity.phcnnode.dto;

import lombok.Data;

@Data
public class ElectricityProcessRequest {
    private String reference;
    private String paymentChannel;
    private double amount;
    private String payerId;
    private String mobile;
    private String type;
    private String channel;
    private String name;
}
