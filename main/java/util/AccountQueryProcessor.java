/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.lemonpay.lemonpayvas.electricity.ikedc.dto.response.PaymentResData;
import com.lemonpay.lemonpayvas.electricity.ikedc.dto.response.ReQueryResponseDto;
import com.lemonpay.lemonpayvas.electricity.ikedc.dto.response.ValidationResDto;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
//import lombok.extern.slf4j.Slf4j;
import java.util.logging.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * @author falodun.ebenezer
 */
//@Slf4j
public class AccountQueryProcessor {

    private Logger log;
    public AccountQueryProcessor(){
        log = Logger.getLogger(AccountQueryProcessor.class.getName());
    }

//    public ValidationResDto validationMeterNo(String meterNo, String accountType, String service, String amount, String channel, String baseUrl, String endpoint, String token, String signaturekey, Logger logger)
//            throws SocketTimeoutException, IOException, Exception {
//
//        ValidationResDto validationResDto = null;
//        HttpURLConnection con = null;
//        String inputLine;
//        String finalUrl = null;
//
//        try {
//            logger.info(":::::::::::::::::::::::entering The meter query METHOD...");
//
//
//            if (endpoint != null) {
//                finalUrl = baseUrl + endpoint;
//
//                logger.info(":::::::::::::::::::::::IKEDC meter validation endpoint >>>> " + endpoint);
//
//                logger.info(":::::::::::::::::::::::IKEDC meter baseurl >>>> " + baseUrl);
//
//                logger.info("::::::::::::::::::::::::::::::: The meter validation Full Url >>>> " + finalUrl);
//
//                ValidationReqDto validationReqDto = new ValidationReqDto(meterNo, accountType, service, amount, channel);
//                Gson gson = new Gson();
//                String USER_AGENT = "Mozilla/5.0";
//                String jsonStringParameters = gson.toJson(validationReqDto);
//
//                ////////////////////////////get the signature
//                logger.info(":::::::::::::::::::::::::::::::::::::::::: calling signature method");
//                RequestSignature requestSignature = new RequestSignature(logger);
//
//                String signature = requestSignature.getSignature(jsonStringParameters, signaturekey);
//
//                logger.info(":::::::::::::::::::::::::JSON REQUEST PARAMETERS for meter validation :::: " + jsonStringParameters);
//
//                logger.info("::::::::::::::::::::::::::::::::::::::the paramters for meter validation: ");
//                logger.info("::::::::::::::::::::::::token " + token);
//                logger.info("::::::::::::::::::::::::signature " + signature);
//                logger.info("::::::::::::::::::::::::account query request " + jsonStringParameters);
//
//                URL url = new URL(finalUrl);
//                con = (HttpURLConnection) url.openConnection();
//                con.setDoOutput(true);
//                con.setConnectTimeout(10000);
//                con.setReadTimeout(10000);
//                con.setRequestMethod("POST");
//                con.setRequestProperty("token", token);
//                con.setRequestProperty("signature", signature);
//                con.setRequestProperty("Content-Type", "application/json; utf-8");
//                con.setRequestProperty("Accept", "application/json");
//                con.setRequestProperty("User-Agent", USER_AGENT);
//
//                try (OutputStream os = con.getOutputStream()) {
//                    byte[] input = jsonStringParameters.getBytes("utf-8");
//                    os.write(input, 0, input.length);
//
//                }
//
//                int responseCode = con.getResponseCode();
//                logger.info("::::::::::::::::::::::::Response Code for KEDC meter validation ::: " + responseCode);
//
//                if (responseCode != 200) {
//                    //logger.error("Failed : HTTP error code : " + responseCode);
//                    logger.info("::::::::::::::::::::::::::Failed : HTTP error code  for IKEDC account query validation is: " + responseCode);
//                    //logger.info("::::::::::::::::::::::::::Failed : HTTP error code  for IKEDC account query validation is: " + responseCode);
//                    // throw new ServerException(":::::::::::::::::::::::::Failed : HTTP error code for IKEDC account query validation is: " + responseCode);
//                }
//
//                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                StringBuffer response = new StringBuffer();
//
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//
//                String output = response.toString();
//                logger.info("::::::::::::::::::::::IKEDC meter validation response >>>> " + output);
//
//                // JSON
//                validationResDto = gson.fromJson(output, ValidationResDto.class);
//            }
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        } finally {
//            if (con != null) {
//                logger.info(":::::::::::::::::::closing the conn");
//                con.disconnect();
//            }
//        }
//
//        return validationResDto;
//
//    }
    public ValidationResDto checkMeter(String meterNo, String vendType, String stringReq, String baseUrl, String endpoint, String token) throws SocketTimeoutException, IOException, Exception {
        String output = "";
        HttpURLConnection con = null;
        String USER_AGENT = "Mozilla/5.0";
        String inputLine;
        BufferedReader in = null;
       
        Gson gson = new Gson();
        ValidationResDto validationResDto = null;

        try {
            log.info(":::::::::::::::::::::::entering The meter query METHOD...");

            if (endpoint != null) {
                String finalUrl = baseUrl + endpoint;

                log.info(":::::::::::::::::::::::IKEDC meter validation endpoint >>>> " + endpoint);

                log.info(":::::::::::::::::::::::IKEDC meter baseurl >>>> " + baseUrl);

                log.info("::::::::::::::::::::::::::::::: The meter validation Full Url >>>> " + finalUrl);

                //String USER_AGENT = "Mozilla/5.0";
                //String jsonStringParameters = gson.toJson(validationReqDto);
                ////////////////////////////get the signature
                log.info(":::::::::::::::::::::::::::::::::::::::::: calling signature method");
                RequestSignature requestSignature = new RequestSignature();

                //String signature = requestSignature.getSignature(jsonStringParameters, signaturekey);
                //logger.info(":::::::::::::::::::::::::JSON REQUEST PARAMETERS for meter validation :::: " + jsonStringParameters);
                log.info("::::::::::::::::::::::::::::::::::::::the paramters for meter validation: ");
                log.info("::::::::::::::::::::::::token " + token);
                //logger.info("::::::::::::::::::::::::signature " + signature);
                log.info("::::::::::::::::::::::::Ikeja " + vendType + " query request " + stringReq);
                System.out.println("::::::::::::::::::::::::Ikeja " + vendType + " query request " + stringReq);
                System.out.println(">>>>>>>>>>>>>>>the auth token is: " + token);
                System.out.println(">>>>>>>>>>>>>>> the url is: " + finalUrl);

                URL url = new URL(finalUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setConnectTimeout(10000);
                con.setReadTimeout(10000);
                con.setRequestMethod("POST");
                con.setRequestProperty("pays-api-key", token);
                //con.setRequestProperty("signature", signature);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("User-Agent", USER_AGENT);

                OutputStream os = con.getOutputStream(); 
                System.out.println(">>>>>>>>>>>>>>>>>>> the request before sending request: "+stringReq);
                os.write(stringReq.getBytes());
                os.flush();
                os.close();
               
                int responseCode = con.getResponseCode();
                log.info("::::::::::::::::::::::::Response Code for KEDC meter validation ::: " + responseCode);

                if (responseCode != 200) {
                    //logger.error("Failed : HTTP error code : " + responseCode);
                    log.info("::::::::::::::::::::::::::Failed : HTTP error code  for IKEDC account query validation is: " + responseCode);
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>.Failed : HTTP error code  for IKEDC account query validation is: " + responseCode);
                    //logger.info("::::::::::::::::::::::::::Failed : HTTP error code  for IKEDC account query validation is: " + responseCode);
                    // throw new ServerException(":::::::::::::::::::::::::Failed : HTTP error code for IKEDC account query validation is: " + responseCode);
                    in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                } else {

                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                }

                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                output = response.toString();
                log.info("::::::::::::::::::::::IKEDC meter validation response >>>> " + output);
                System.out.println("::::::::::::::::::::::IKEDC meter validation response >>>> " + output);
                
                if(responseCode == 200){
                    validationResDto = gson.fromJson(output, ValidationResDto.class);
                }else{
                   JsonObject queryFailed = gson.fromJson(output,JsonObject.class);
                   validationResDto = new ValidationResDto();
                   validationResDto.setSuccess(queryFailed.get("success").getAsBoolean());
                   validationResDto.setMessage(queryFailed.get("message").getAsString());
                }

            }
        }catch(SocketTimeoutException e){
           e.printStackTrace();
            validationResDto = new ValidationResDto(false,EnumResponseMsg.TIMEOUT.responseMsg,null);
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> the error is: "+e.getMessage());
        } 
        catch (Exception e) {

            e.printStackTrace();
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> failed");
            validationResDto = new ValidationResDto(false,EnumResponseMsg.FAILED.responseMsg,null);
        } finally {
            if (con != null) {
                log.info(":::::::::::::::::::closing the conn");
                con.disconnect();
            }
        }

        log.info(">>>>>>>>>>>>>>>>>>>>>> the response query Processor is: "+gson.toJson(validationResDto));
        return validationResDto;
    }

    public ReQueryResponseDto requery(String reference, String baseUrl, String requeryeNDPOINT, String token)  throws SocketTimeoutException, IOException, Exception {
        
        String output = "";
        HttpURLConnection con = null;
        String USER_AGENT = "Mozilla/5.0";
        String inputLine;
        BufferedReader in = null;
       
        Gson gson = new Gson();
        PaymentResData paymentResDto = null;
        ReQueryResponseDto reQueryResDto = null;

        try {
            log.info(":::::::::::::::::::::::entering The meter query METHOD...");

            if (requeryeNDPOINT != null) {
                String finalUrl = baseUrl + requeryeNDPOINT + reference;

                log.info(":::::::::::::::::::::::IKEDC meter baseurl >>>>>" + baseUrl);
                System.out.println(":::::::::::::::::::::::IKEDC meter baseurl >>>>>" + baseUrl);
                
                log.info(":::::::::::::::::::::::::::::::The meter validation Full Url >>>>" + finalUrl);

                //String USER_AGENT = "Mozilla/5.0";
                //String jsonStringParameters = gson.toJson(validationReqDto);
                ////////////////////////////get the signature
                log.info(":::::::::::::::::::::::::::::::::::::::::: calling signature method");
                RequestSignature requestSignature = new RequestSignature();

                //String signature = requestSignature.getSignature(jsonStringParameters, signaturekey);
                //logger.info(":::::::::::::::::::::::::JSON REQUEST PARAMETERS for meter validation :::: " + jsonStringParameters);
                log.info("::::::::::::::::::::::::::::::::::::::the paramters for meter validation: ");
                log.info("::::::::::::::::::::::::token " + token);
                //logger.info("::::::::::::::::::::::::signature " + signature);
//                logger.info("::::::::::::::::::::::::Ikeja " + vendType + " query request " + stringReq);
//                System.out.println("::::::::::::::::::::::::Ikeja " + vendType + " query request " + stringReq);
                System.out.println(">>>>>>>>>>>>>>>the auth token is: " + token);
                System.out.println(">>>>>>>>>>>>>>> the url is: " + finalUrl);

                URL url = new URL(finalUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setConnectTimeout(10000);
                con.setReadTimeout(10000);
                con.setRequestMethod("GET");
                con.setRequestProperty("pays-api-key", token);
                //con.setRequestProperty("signature", signature);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("User-Agent", USER_AGENT);

//                try ( OutputStream os = con.getOutputStream()) {
//                    byte[] input = stringReq.getBytes("utf-8");
//                    os.write(input, 0, input.length);
//
//                }

                int responseCode = con.getResponseCode();
                String responseMsg = con.getResponseMessage();
                
                log.info("::::::::::::::::::::::::Response Code for KEDC requery ::: " + responseCode);
                System.out.println("::::::::::::::::::::::::Response Code for IKEDC requery ::: " + responseCode);
                System.out.println("::::::::::::::::::::::::Response msg for IKEDC requery ::: " + responseMsg);

                if (responseCode == 200 || responseCode ==201) {
                   
                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {
                    
                     //logger.error("Failed : HTTP error code : " + responseCode);
                    log.info("::::::::::::::::::::::::::Failed : HTTP error code  for requery is: " + responseCode);
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>Failed : HTTP error code  for requery is: " + responseCode);
                    //logger.info("::::::::::::::::::::::::::Failed : HTTP error code  for IKEDC account query validation is: " + responseCode);
                    // throw new ServerException(":::::::::::::::::::::::::Failed : HTTP error code for IKEDC account query validation is: " + responseCode);
                    in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }

                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                output = response.toString();
                log.info("::::::::::::::::::::::IKEDC transaction requery response from ikedc is >>>>>>>>>>>>>> " + output);
                System.out.println("::::::::::::::::::::::IKEDC transaction requery response from ikedc is >>>>>>>>>>>>>> " + output);
               
                reQueryResDto = gson.fromJson(output, ReQueryResponseDto.class);
                
                /////update from 202 to responseCode
                reQueryResDto.setCode(responseCode);

            }
        } catch (Exception e) {

            e.printStackTrace();
            reQueryResDto = gson.fromJson(output, ReQueryResponseDto.class);
        } finally {
            if (con != null) {
                log.info(":::::::::::::::::::closing the conn");
                con.disconnect();
            }
        }

        return reQueryResDto;
    }

}
