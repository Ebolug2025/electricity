package com.etranzact.vasgate.electricity.ekoedc.Domain.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Customer {
    private String meterSerial;
    private String collectionInd;
    private String account;
    private String accountBalance;
    private String tariffDescription;
    private String indicatorPrePostAccount;
    private String serviceAddress;
    private String name;
    private String districtName;
}
