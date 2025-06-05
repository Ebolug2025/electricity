package com.etranzact.vasgate.electricity.ekoedc.Domain.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@Data
@AllArgsConstructor
public class PaymentValueResponse {
    private String account;
    private double accountBalance;
    private double amountLast;
    private String comment;
    private String customerName;
    private double debtPayment;
    private long lastPaymentDate;
    private String meterSerial;
    private double percentageDebt;
    private String serviceAddress;
    private String tariffDescription;
    private double units;
    private double unitsPayment;

    private String unitsType;


    private List<UnitTopUp> unitTopUp;
}
