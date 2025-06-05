package com.etranzact.vasgate.electricity.dto;

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
//    private String action;
    @NotBlank(message = "paymentChannel is required")
    private String paymentChannel;
    @NotBlank(message = "mobile is required")
    private String mobile;
    @NotBlank(message = "merchantCode is required")
    private String merchant;
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "bank is required")
    private  String bank;
    @NotBlank(message = "customerAddress is required")
    private String customerAddress;
    @NotBlank(message = "uniqueTransId is required")
    private String uniqueTransId;
    @NotBlank(message = "client is required")
    private String client;

}
