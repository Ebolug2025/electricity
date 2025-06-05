package com.etranzact.vasgate.electricity.eedc.actionenum;

public enum EnumResponseMsg {

    SUCCESS("00","success"),
    TIMEOUT("408","connection timeout"),
    FAILED("01","failed"),
    REQUERY_DOES_NOT_EXIST("05", "REQUERY_DOES_NOT_EXIST"),
    INVALID_METER_TYPE("01","Invalid meter type"),
    NOT_FOUND("400","BAD REQUEST"),
    WORNG_MOBILE("07","Invalid mobile number"),
    TOKENERROR("06","Unable to generate token"),
    ERRORGETINGPRODUCTcODE("02","unable to get product code"),

    ACCESS_CODE_NOT_FOUND("404","ACCESS_CODE_NOT_FOUND");

    public String responseCode;
    public String responseMsg;
    public static String successerrorCode = "00";
    public static String business_unit = "Ikeja electricity";
    EnumResponseMsg(String responseCode, String responseMsg) {

        this.responseCode = responseCode;
        this.responseMsg = responseMsg;

    }
}
