package com.etranzact.vasgate.electricity.kedc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meter {
    private long id;
    private double account_number;
    private double meter_number;
    private String address;
    private Customer customer;
    private double minimum_purchase;
    private String tariff;
    private String service_band;
    private Boolean validated;
    private double nin;
    private String account_status;
    private String region;
    private String csp;


}
