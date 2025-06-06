/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.lemonpay.lemonpayvas.electricity.ikedc.dto.request.AuthRequestDto;
import com.lemonpay.lemonpayvas.electricity.ikedc.dto.response.AuthResDto;
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
public class IKEDCAuthTokenUtil {
    

    
    public IKEDCAuthTokenUtil(){

    }

    public AuthResDto getToken(String wallet, String username, String password, String identifier, String baseUrl, String endpoint) throws SocketTimeoutException, IOException, Exception{

        log.info(":::::::::::::::::::::::Auth generation METHOD...");
        
        AuthResDto authResDto = null;
        HttpURLConnection con = null;
        String inputLine;
        String finalUrl = null;

        try {
            log.info("::::::::::::::::::::IKEDC tokenEndpoint >>>> " + endpoint);

            if (endpoint != null) {
                finalUrl = baseUrl + endpoint;

                log.info("Postpaid IKEDC token Final Url >>>> " + finalUrl);

                AuthRequestDto authRequestDto = new AuthRequestDto(wallet, username, password, identifier);
                Gson gson = new Gson();
                String USER_AGENT = "Mozilla/5.0";
                String jsonStringParameters = gson.toJson(authRequestDto);

                log.info(":::::::::::::::::::::::::JSON REQUEST PARAMETERS for token generation :::: " + jsonStringParameters);

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
                    //logger.error("Failed : HTTP error code : " + responseCode);
                    log.info("::::::::::::::::::::Failed : HTTP error code : " + responseCode);
                    throw new ServerException("Failed : HTTP error code : " + responseCode);
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String output = response.toString();
                log.info("::::::::::::::::::::::::::::::Postpaid Payment Transaction Response >>>> " + output);

                // JSON
                authResDto = gson.fromJson(output, AuthResDto.class);
            }
        } finally {
            if (con != null) {
                log.info(":::::::::::::::::::closing the conn");
                con.disconnect();
            }
        }

        return authResDto;
       
    }
}