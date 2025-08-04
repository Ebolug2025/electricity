package com.lemonpay.lemonpayvas.electricity.eedc.enums;

public enum Response {

    INVALID_ACCOUNT_NUMBER("400"),
    DELIVERED("00"),
    INITIATED("05"),
    INVALID_AMOUNT("400"),
    INVALID_ACCOUNT_METER_TYPE("400"),
    ERROR_OCCURED("1"),
    SUCCESS("00"),
    PENDING("05"),
    FAILED("01"),
    TRANSACTION_IS_REPROCESSING("099"), PERFORM_QUERY_FIRST("400");
    public String code;

    private Response(String code){
        this.code = code;
    }

}
