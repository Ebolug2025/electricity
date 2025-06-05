package com.etranzact.vasgate.electricity.jedc.utils;

import java.util.HashMap;
import java.util.Map;

public class AccessCodeSingleton {

    private Map<String, String> accessCodeMap = new HashMap();;

    private static AccessCodeSingleton accessCodeSingleton;

    private AccessCodeSingleton(){

        accessCodeMap = new HashMap();
    }

    public void setAccessCode(String account, String accessCode){

        accessCodeMap.put(account, accessCode);
    }

    public String getAccessCode(String account){

        if(accessCodeMap.containsKey(account)){
            return accessCodeMap.get(account);
        }else{
            return null;
        }
    }

    public void removeAccessCode(String account){

        accessCodeMap.remove(account);
    }

    public int getSize(){

        return accessCodeMap.size();
    }

    public static AccessCodeSingleton getInstance(){

        if(accessCodeSingleton == null){

            accessCodeSingleton = new AccessCodeSingleton();

            return accessCodeSingleton;
        }else{

            return accessCodeSingleton;
        }
    }
}
