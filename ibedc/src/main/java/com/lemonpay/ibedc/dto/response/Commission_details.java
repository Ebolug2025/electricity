package com.lemonpay.ibedc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commission_details{
    private String amount;
    private String rate;
    private String rate_type;
    private String computation_type;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRate_type() {
        return rate_type;
    }

    public void setRate_type(String rate_type) {
        this.rate_type = rate_type;
    }

    public String getComputation_type() {
        return computation_type;
    }

    public void setComputation_type(String computation_type) {
        this.computation_type = computation_type;
    }
}
