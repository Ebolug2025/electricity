package com.etranzact.vasgate.electricity.ekoedc.Domain.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UnitTopUp {
    private String concept;
    private double units;
    private double price;
    private double amount;
    private String conceptName;
}
