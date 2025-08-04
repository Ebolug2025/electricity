package com.lemonpay.lemonpayvas.electricity.ekedc.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommissionDetails {

    private Double amount;
    private String rate;
    private String rate_type;
    private String computation_type;
}
