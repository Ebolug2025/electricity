package com.etranzact.vasgate.electricity.ekoedc.Domain.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class VendorInfoResponse {

    private String idVendor;
    private String descVendor;
    private String district;
    private double balance;
    private int displayBalance;
    private int lowBalanceAlert;
}
