package com.lemonpay.lemonpayvas.electricity.ekedc.enums;

public enum Response {

    INVALID_ACCOUNT_NUMBER("400"),
    INVALID_AMOUNT("400"),
    INVALID_ACCOUNT_METER_TYPE("400"),
    ERROR_OCCURED("1"),
    SUCCESS("00"),
    PENDING("5"),
    FAILED("1"),
    TRANSACTION_IS_REPROCESSING("099");
    public String code;

    private Response(String code){
        this.code = code;
    }

}
