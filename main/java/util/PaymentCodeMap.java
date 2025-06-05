/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ugochukwu.omeje
 */
public class PaymentCodeMap {
    
    private static Map<String, String> paymentCodeMap = new HashMap<String, String>();
    
    public static String getPaymentCode(String meterNo){
     
        return paymentCodeMap.get(meterNo);
    }
    
    public static void setPaymentCode(String paymentCode, String meterNo){
        
        paymentCodeMap.put(meterNo, paymentCode);
    }
    
    public static void removePaymentCode(String meterNo){
        
        paymentCodeMap.remove(meterNo);
    }

    public static int getPaymentCodeSize() {

        return paymentCodeMap.size();
    }

   
}
