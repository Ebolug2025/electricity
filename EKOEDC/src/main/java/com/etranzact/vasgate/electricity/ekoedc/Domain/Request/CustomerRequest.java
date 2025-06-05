package com.etranzact.vasgate.electricity.ekoedc.Domain.Request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CustomerRequest {
    private String idVendor;
    private String codUser;
    private String codType;
    private String value;
    private double totalPayment;
}
