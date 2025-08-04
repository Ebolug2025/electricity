package com.lemonpay.lemonpayvas.electricity.ekedc.responsedto;

import lombok.Data;

@Data
public class Transactions {

    private String status;
    private String product_name;
    private String unique_element;
    private String unit_price;
    private String quantity;
    private String service_verification;
    private String channel;
    private double commission;
    private double total_amount;
    private double discount;
    private String type;
    private String email;
    private String phone;
    private String name;
    private String convinience_fee;
    private String amount;
    private String platform;
    private String method;
    private String transactionId;
    private ProcessCommissionDetails commission_details;

}
