/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.vasgate.electricity.kedc.utils;

import com.etranzact.vasgate.electricity.kedc.enums.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * 
 */
@Slf4j
public class HttpUtil {

   private final static String USER_AGENT = "Mozilla/5.0";

   public static String []sendGet(String url,String accesscode,String origin) throws Exception {

      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      // optional default is GET
      con.setRequestMethod("GET");

      //add request header
      con.setRequestProperty("User-Agent", USER_AGENT);
      con.setRequestProperty("Authorization", "Bearer "+accesscode);
      con.setRequestProperty("Origin", origin);

      int responseCode = con.getResponseCode();
      log.info("Sending 'GET' request to URL : " + url);
      log.info("Response Code : " + responseCode);

      BufferedReader in = new BufferedReader(
      new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
         response.append(inputLine);
      }
      in.close();
      String[]resp = {responseCode+"",response.toString()};
      //print result
      log.info(response.toString());
      return  resp;
   }

   public static String[] sendPost(String url, String jsonString,String accesscode,String origin, int timeout) throws Exception {
      System.out.println("\nSending 'POST' request to URL : " + url);
      log.info("\nSending 'POST' request to URL : " + url);
     System.out.println("Post parameters : " + jsonString);
      log.info("Post parameters : " + jsonString);
      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setReadTimeout(timeout*1000);
      con.setRequestMethod("POST");
      con.setRequestProperty("User-Agent", USER_AGENT);
      con.setRequestProperty("Content-Type", "application/json");
      con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
      con.setRequestProperty("Authorization", "Bearer "+accesscode);
      con.setRequestProperty("Origin", origin);
      con.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(con.getOutputStream());
      wr.writeBytes(jsonString);
      wr.flush();
      wr.close();

      int responseCode = con.getResponseCode();
      System.out.println("Response Code : " + responseCode);
      log.info("Response Code : " + responseCode);


      BufferedReader in = new BufferedReader(
              new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
         response.append(inputLine);
         System.out.println("========== response for payment is: "+response);
      }
      in.close();
      String[]resp = {responseCode+"",response.toString()};
      //print result
      return resp;

   }

    public static String getChannel(String uniqueTransId, String paymentChannel) {
        String channel = Channel.TRANSFER.name();
        String channelKey = uniqueTransId.substring(0, 2);
        if (!paymentChannel.startsWith("0") && channelKey.equals("09")) {
            channel = Channel.TRANSFER.name();
        } else {
            if (channelKey.equals("01") || channelKey.equals("09")) {
                channel = Channel.TRANSFER.toString();
            } else if (channelKey.equals("02")) {
                if (uniqueTransId.contains("02USD")) {
                    channel = Channel.TRANSFER.name();
                } else if (uniqueTransId.contains("02POS")) {
                    channel = Channel.TRANSFER.name();
                } else {
                    channel = Channel.TRANSFER.name();
                    //channel =  //mobile
                }
            } else if (channelKey.equals("03")) {
                channel = Channel.TRANSFER.name();
            } else if (channelKey.equals("05")) {
                channel = Channel.CARD.name();

            } else if (channelKey.equals("11")) {
                channel = Channel.CARD.name();

            } else {
                channel = Channel.TRANSFER.name();
            }
        }

        return channel;
    }

}
