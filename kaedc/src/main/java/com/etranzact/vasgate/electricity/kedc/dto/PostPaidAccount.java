package com.etranzact.vasgate.electricity.kedc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostPaidAccount {

    private Integer id;
    private String account_number;
    private String address;
    private Double arrears;
    private String tariff;
    private String tariff_rate;
    private boolean is_md;
    private String service_band;
    private boolean validated;
    private String nin;
    private String account_status;
    private String region;
    private String csp;
    private KanoCustomer customer;
    private LastBill last_bill;

}
