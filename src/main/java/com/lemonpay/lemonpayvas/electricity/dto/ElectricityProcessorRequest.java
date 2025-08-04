package com.lemonpay.lemonpayvas.electricity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ElectricityProcessorRequest {
    @NotBlank(message = "reference is required")
    private String reference;
    @NotBlank(message = "alias is required")
    private String alias;
    @NotBlank(message = "type is required")
    private String type;
    @NotBlank(message = "channel is required")
    private String channel;
    @NotBlank(message = "amount is required")
    private Double amount;
    @NotBlank(message = "payerId is required")
    private String payerId;
    @NotBlank(message = "paymentChannel is required")
    private String mobile;
    private String merchant;
    private String name;
    private  String bank;
    private String customerAddress;
    private String uniqueTransId;
    private String client;

}
