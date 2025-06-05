/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author ugochukwu.omeje
 */
public enum EnumResponseMsg {
    
        SUCCESS("00","success"),
        TIMEOUT("408","connection timeout"),
        FAILED("01","failed"),
        WORNG_MOBILE("07","Invalid mobile number"),
        TOKENERROR("06","Unable to generate token"),
        ERRORGETINGPRODUCTcODE("02","unable to get product code");
    
    public String responseCode;
    public String responseMsg;
    public static String successerrorCode = "00";
    public static String business_unit = "Ikeja electricity";
    private EnumResponseMsg(String responseCode, String responseMsg){
    
    this.responseCode = responseCode;
    this.responseMsg = responseMsg;
}


}
