package com.lemonpay.ibedc.Action;

public enum EnumResponseMsg {
    SUCCESS("00","success"),

    INVALID_PARAMETERS("400","MISSING ACCESS_CODE: CALL QUERY"),
    TIMEOUT("408","connection timeout"),
    FAILED("01","failed"),
    REQUERY_DOES_NOT_EXIST("05", "REQUERY_DOES_NOT_EXIST"),
    WORNG_MOBILE("07","Invalid mobile number"),
    DISCOERROR("06","Unable to generate token");

    public String responseCode;
    public String responseMsg;
    public static String successerrorCode = "000";
    public static String business_unit = "";
    private EnumResponseMsg(String responseCode, String responseMsg){

        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
    }
}
