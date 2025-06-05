/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.vasgate.electricity.ikedc.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ugochukwu.omeje
 */
public class ChannelMap {
    
    public ChannelMap(){
        
        mapchannel.put("01", "WEB");
        mapchannel.put("02", "MOBILE");
        mapchannel.put("02USD", "MOBILE");
        mapchannel.put("02POS", "ANDROIDPOS");
        mapchannel.put("03", "ANDROIDPOS");
        mapchannel.put("05", "WEB");
        mapchannel.put("11", "MOBILE");
        mapchannel.put("09", "WEB");
      
    }
    
    public  Map<String, String> mapchannel = new HashMap<String, String>();
    
    public String getChannel(String key){
        
        return mapchannel.get(key);
    }
    
   public String getChannel(String channelKey, String clientReference){
       String channel = null;
                    if (channelKey.equals("01") || channelKey.equals("09")) {
                        channel = getChannel("01");
                    } else if (channelKey.equals("02")) {
                        if (clientReference.contains("02USD")) {
                            channel = getChannel("02USD");
                        } else if (clientReference.contains("02POS")) {
                            channel = getChannel("02POS");
                        } else {
                            channel = getChannel("02");
                        }
                    } else if (channelKey.equals("03")) {
                        channel = getChannel("03");
                    } else if (channelKey.equals("11")) {
                        channel = getChannel("02");
                    } else if (channelKey.equals("05")) {
                        channel = getChannel("05");
                    } else {
                        channel =getChannel("05");
                    }
                    
                    return channel;
                }
}
