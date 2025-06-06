/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.lemonpay.lemonpayvas.electricity.ikedc.dto.request.EncPinReqDto;
import com.lemonpay.lemonpayvas.electricity.ikedc.dto.response.EncPinResDto;
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
 *
 * @author ugochukwu.omeje
 */
@Slf4j
public class IKEDCEncPinGenerator {

    
  //  private Logger logger;
//    public IKEDCEncPinGenerator() {
//        
//        logger = Logger.getLogger(IKEDCEncPinGenerator.class.getName());
//    }
 
    public EncPinResDto getEncryptedPin(String wallet, String username, String password, String identifier, String baseUrl, String endpoint)
            throws SocketTimeoutException, IOException, Exception{
        
         log.info(":::::::::::::::::::::::Enc pin generation METHOD...");
        
        EncPinResDto encPinResDto = null;
        HttpURLConnection con = null;
        String inputLine;
        String finalUrl = null;

        try {
            log.info(":::::::::::::::::::::::; KEDC Pinc generation endpoint >>>> " + endpoint);

            if (endpoint != null) {
                finalUrl = baseUrl + endpoint;

                log.info("::::::::::::::::::::::::::::::: The Encrypted pin generation Full Url >>>> " + finalUrl);

                EncPinReqDto encPinReqDto = new EncPinReqDto(wallet, username, password, identifier);
                Gson gson = new Gson();
                String USER_AGENT = "Mozilla/5.0";
                String jsonStringParameters = gson.toJson(encPinReqDto);

                log.info(":::::::::::::::::::::::::JSON REQUEST PARAMETERS for pin generation :::: " + jsonStringParameters);
                System.out.println(":::::::::::::::::::::::::JSON REQUEST PARAMETERS for pin generation :::: " + jsonStringParameters);

                URL url = new URL(finalUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setConnectTimeout(10000);
                con.setReadTimeout(10000);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("User-Agent", USER_AGENT);
                
                try ( OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonStringParameters.getBytes("utf-8");
                    os.write(input, 0, input.length);

                }

                int responseCode = con.getResponseCode();
                log.info("::::::::::::::::::::::::Response Code ::: " + responseCode);

                if (responseCode != 200) {
                    //log.error("Failed : HTTP error code : " + responseCode);
                    log.info("::::::::::::::::::::::::::Failed : HTTP error code  for KEDC encrypted pin generation is: " + responseCode);
                    throw new ServerException(":::::::::::::::::::::::::Failed : HTTP error code for KEDC encrypted pin generation is: " + responseCode);
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String output = response.toString();
                log.info("::::::::::::::::::::::encrypted pin generation response >>>> " + output);

                // JSON
                encPinResDto = gson.fromJson(output, EncPinResDto.class);
                log.info(":::::::::::::::::::::::::::::::::the encrypted pin gotten is "+encPinResDto.getData().getPin());
                System.out.println(":::::::::::::::::::::::::::::::::the encrypted pin gotten is "+encPinResDto.getData().getPin());
            }
        } finally {
            if (con != null) {
                log.info(":::::::::::::::::::closing the conn");
                con.disconnect();
            }
        }

        return encPinResDto;
       
    }
    
}
