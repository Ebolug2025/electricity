package com.lemonpay.lemonpayvas.electricity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ElectricityGenericQueryRequest {
    @NotBlank(message = "reference is required")
    private String reference;
    @NotBlank(message = "alias is required")
    private String alias;
    @NotBlank(message = "type is required")
    private String type;
    @NotBlank(message = "channel is required")
    private String channel;
    private Double amount;
    @NotBlank(message = "payerId is required")
    private String payerId;
    private String mobile;

}
