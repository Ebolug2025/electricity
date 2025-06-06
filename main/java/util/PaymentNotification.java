/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.lemonpay.lemonpayvas.electricity.ikedc.dto.request.PaymentReqDto;
import com.lemonpay.lemonpayvas.electricity.ikedc.dto.response.PaymentResData;
import com.lemonpay.lemonpayvas.electricity.ikedc.dto.response.PaymentResDto;
import com.lemonpay.lemonpayvas.electricity.ikedc.dto.response.PaymentResErrorDto;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.rmi.ServerException;


/**
 * @author ugochukwu.omeje
 */
@Slf4j
public class PaymentNotification {

//    private Logger log;

    public PaymentNotification() {

     //   this.log = logger;
    }

    public PaymentResData sendPaymentNotification(String customerPhoneNo, String paymentMethod, String service, String
            clientReference, String pin, String productCode, String baseUrl, String endpoint, String token, String key)
            throws SocketTimeoutException, IOException, Exception {


        log.info(":::::::::::::::::::::::The KEDC payment notification request...");

        PaymentResData paymentResDto = null;
        HttpURLConnection con = null;
        String inputLine;
        String finalUrl = null;

        try {
            log.info(":::::::::::::::::::::::: KEDC meter validation endpoint >>>> " + endpoint);

            if (endpoint != null) {
                finalUrl = baseUrl + endpoint;

                log.info("::::::::::::::::::::::::::::::: The KEDC payment notification Full Url >>>> " + finalUrl);

                PaymentReqDto paymentReqDto = new PaymentReqDto(customerPhoneNo, paymentMethod, service, clientReference, pin, productCode, null);
                Gson gson = new Gson();
                String USER_AGENT = "Mozilla/5.0";
                String jsonStringParameters = gson.toJson(paymentReqDto);

                RequestSignature requestSignature = new RequestSignature();

                String signature = requestSignature.getSignature(jsonStringParameters, key);

                log.info(":::::::::::::::::::::::::JSON REQUEST for payment notification ::::");

                log.info("::::::::::::::::::::::::token " + token);
                log.info("::::::::::::::::::::::::signature " + signature);
                log.info("::::::::::::::::::::::::request " + jsonStringParameters);

                URL url = new URL(finalUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setConnectTimeout(10000);
                con.setReadTimeout(10000);
                con.setRequestMethod("POST");
                con.setRequestProperty("token", token);
                con.setRequestProperty("signature", signature);
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("User-Agent", USER_AGENT);

                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonStringParameters.getBytes("utf-8");
                    os.write(input, 0, input.length);

                }

                int responseCode = con.getResponseCode();
                log.info("::::::::::::::::::::::::Response Code for IKEDC payment notification ::: " + responseCode);

                if (responseCode != 200) {
                    //logger.info("Failed : HTTP error code : " + responseCode);
                    log.info("::::::::::::::::::::::::::Failed : HTTP error code  for IKEDC payment notification is: " + responseCode);
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    log.info(":::::::::::::::::failed response is: " + response.toString());
                    throw new ServerException(":::::::::::::::::::::::::Failed : HTTP error code for KEDC payment notification is: " + responseCode);
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String output = response.toString();
                log.info("::::::::::::::::::::::The IKEDC payment notification response >>>> " + output);

                paymentResDto = gson.fromJson(output, PaymentResData.class);
            }
        } finally {
            if (con != null) {
                log.info(":::::::::::::::::::closing the conn");
                con.disconnect();
            }
        }

        return paymentResDto;

    }

    public PaymentResDto vendMeter(String strPayLoad, String vendType, String baseUrl, String endPoint, String token) throws SocketTimeoutException, IOException, Exception {
        log.info("............................INSIDE IKEDC vendMeter for notification................................. ");
        HttpURLConnection con = null;
        String inputLine;
        String output = "";
        String finalUrl = null;
        Gson gson = new Gson();
        PaymentResDto paymentResDto = null;
        String USER_AGENT = "Mozilla/5.0";
        int responseCode = 0;
        
        try {
            finalUrl = baseUrl + endPoint;
            log.info("................ IKEDC vendMeter for notification url :::: " + finalUrl);
            System.out.println("................ IKEDC vendMeter for notification url :::: " + finalUrl);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>> the payload sent is: "+strPayLoad);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>> the token sent is: "+token);
            URL url = new URL(finalUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setConnectTimeout(10000);
            con.setRequestMethod("POST");
            con.setRequestProperty("pays-api-key", token);
            con.setRequestProperty("Content-Type", "application/json");

//            try (OutputStream os = con.getOutputStream()) {
//                byte[] input = jsonStringParameters.getBytes("utf-8");
//                os.write(input, 0, input.length);
//
//            }
            OutputStream os = con.getOutputStream();
            os.write(strPayLoad.getBytes());
            os.flush();
            os.close();
            responseCode = con.getResponseCode();
            log.info("::::::::::::::::::::::::Response Code for IKEDC payment notification ::: " + responseCode);
            BufferedReader in;
            StringBuffer response = new StringBuffer();
            if (responseCode == 200 || responseCode == 202) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            } else {
                in = new BufferedReader(new InputStreamReader(
                        con.getErrorStream()));

            }
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            output = responseCode + "~" + response.toString();
            
            log.info("::::::::::::::::::::::Response from  IKEDC vendMeter for notification >>>> " + output);
            System.out.println("::::::::::::::::::::::Response from  IKEDC vendMeter for notification >>>> " + output);
            
            if(responseCode == 200 || responseCode == 202){
//                paymentResDto  = gson.fromJson(output.split("~")[1], PaymentResDto.class);
                paymentResDto = gson.fromJson(output.split("~")[1], PaymentResDto.class);

            }else{
               paymentResDto = new PaymentResDto();
                PaymentResErrorDto paymentResErrorDto = gson.fromJson(output.split("~")[1], PaymentResErrorDto.class);
                 System.out.println(">>>>>>>>>>>>>>>>> the failed response is: "+paymentResErrorDto);
                paymentResDto.setMessage(paymentResErrorDto.getMessage());
                paymentResDto.setSuccess(paymentResErrorDto.isSuccess());
                }
        } catch (Exception e) {
            e.printStackTrace();
            responseCode = con.getResponseCode();
            paymentResDto = new PaymentResDto();
            PaymentResErrorDto paymentResErrorDto = gson.fromJson(output, PaymentResErrorDto.class);
            paymentResDto.setMessage(paymentResErrorDto.getMessage());
            paymentResDto.setSuccess(paymentResErrorDto.isSuccess());
        } finally {
            if (con != null) {//
                log.info(":::::::::::::::::::closing the conn");
                con.disconnect();
            }
        }
        paymentResDto.setStatus(responseCode);

       
        System.out.println(">>>>>>>>>>>>>>>>>>>> the paymentResDto is: "+gson.toJson(paymentResDto));
        log.info(">>>>>>>>>>>>>>>>>>>> the paymentResDto is: "+gson.toJson(paymentResDto));
        return paymentResDto;
    }


}
