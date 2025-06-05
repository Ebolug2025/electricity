package com.etranzact.vasgate.electricity.ekoedc.Domain.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectricityReQueryRequest {

    private String reference;
    private String paymentChannel;
    private double amount;
    private String account;
    private String mobile;
    private String type;
    private String type2;
    private String uniqueTransId;
    private String action;

}
