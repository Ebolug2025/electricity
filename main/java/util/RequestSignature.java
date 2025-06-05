/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import lombok.extern.slf4j.Slf4j;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author ugochukwu.omeje
 */
@Slf4j
public class RequestSignature {
    
   // private Logger log =null;
    
    public RequestSignature(){

    }
    
    public String getSignature(String request, String KEY)throws InvalidKeyException, NoSuchAlgorithmException{
        
        log.info("::::::::::::::::::::::::::::::::::::: the request for getting signature and key: "+request +" "+" key "+KEY);
        
        return generateHmac256(request, KEY.getBytes());
    }
    
    public String generateHmac256(String message, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
        byte[] bytes = hmac("HmacSHA256", key, message.getBytes());
        return bytesToHex(bytes);   
    }

    byte[] hmac(String algorithm, byte[] key, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(message);
    }

    String bytesToHex(byte[] bytes) {   
        final char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0, v; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
