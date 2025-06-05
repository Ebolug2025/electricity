package com.etranzact.vasgate.electricity.jedc.utils;

import com.etranzact.vasgate.electricity.jedc.dto.*;
import com.etranzact.vasgate.electricity.jedc.dto.VerifyPayment;
//import com.etranzact.vasgate.jedc.dto.*;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.rmi.ServerException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.logging.Logger;
@Slf4j
public class JosService {
    public QueryResponse customerQuery(String meter, String action, String baseUrl, String endpoint, String token, String private_key) throws SocketTimeoutException, IOException, Exception {
        log.info(":::::::::::::::::::::IN " + action + " ACCOUNT VERIFICATION METHOD");
        //log.info("::::::::::::::::::::::The token and privaet_key are: " + token + " " + private_key);
        QueryResponse customerResponse = null;
        HttpURLConnection con = null;
        String finalurl = null;

        String inputLine;
        try {
            log.info(":::::::::::::::::::::" + action + " Account Verification Endpoint >>>> " + endpoint);

            if (endpoint != null) {
                // endpoint = endpoint + "core/energy/jos/prepaid/live/customer/" + meter;  // LIVE URL
                endpoint = endpoint + "?customer=" + meter;
                finalurl = baseUrl + endpoint;
                String USER_AGENT = "Mozilla/5.0";
                log.info(":::::::::::::::::" + action + " Account Verification Final Url >>>> " + finalurl);

                URL url = new URL(finalurl);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("PRIVATE-KEY", private_key.trim());
                con.setRequestProperty("TOKEN", token.trim());
                con.setRequestProperty("User-Agent", USER_AGENT);

                int responseCode = con.getResponseCode();

                if (responseCode != 200) {
                    // log.error(":::::::::::::::::::::Failed : HTTP error code : " + responseCode);
                    log.info(":::::::::::::::::::::Failed : HTTP error code : " + responseCode);
                    log.info("::::::::::::::::::::::failed : the message is: " + con.getResponseMessage());
                    throw new ServerException("Failed : HTTP error code : " + responseCode);
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String output = response.toString();
                log.info("::::::::::::::::::::::::" + action + " Account Verification Response >>>> " + output);

                // JSON
                Gson gson = new Gson();
                customerResponse = gson.fromJson(output, QueryResponse.class);
            }
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return customerResponse;
    }

    public PaymentResponse customPayment(String accessCode, String action, double amount, String mobile, String baseUrl, String endpoint, String token, String private_key) throws SocketTimeoutException, IOException, Exception {
        log.info(":::::::::::::::::::::::IN POSTPAID TRANSACTION PAYMENT METHOD...");


        log.info(":::::::::::::::::::::IN " + action + " ACCOUNT VERIFICATION METHOD");
        log.info("::::::::::::::::::::::The token and privaet_key are: " + token + " " + private_key);

        PaymentResponse paymentResponse = null;
        HttpURLConnection con = null;
        String inputLine;
        String finalUrl = null;

        try {
            log.info("Postpaid Transaction Payment Endpoint >>>> " + endpoint);

            if (endpoint != null) {
                finalUrl = baseUrl + endpoint;

                log.info("Postpaid Transaction Payment Final Url >>>> " + finalUrl);

                PaymentRequest paymentRequest = new PaymentRequest(accessCode, amount, mobile);
                Gson gson = new Gson();
                String USER_AGENT = "Mozilla/5.0";
                String jsonStringParameters = gson.toJson(paymentRequest);

                log.info(":::::::::::::::::::::::::JSON REQUEST PARAMETERS:::: " + jsonStringParameters);

                URL url = new URL(finalUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setConnectTimeout(10000);
                con.setReadTimeout(10000);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("PRIVATE-KEY", private_key);
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("TOKEN", token);

                try ( OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonStringParameters.getBytes("utf-8");
                    os.write(input, 0, input.length);

                }

                int responseCode = con.getResponseCode();
                log.info("::::::::::::::::::::::::Response Code ::: " + responseCode);

                if (responseCode != 200) {
                    //log.error("Failed : HTTP error code : " + responseCode);
                    log.info("Failed : HTTP error code : " + responseCode);
                    throw new ServerException("Failed : HTTP error code : " + responseCode);
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String output = response.toString();
                log.info("Postpaid Payment Transaction Response >>>> " + output);

                // JSON
                paymentResponse = gson.fromJson(output, PaymentResponse.class);
            }
        } finally {
            if (con != null) {
                log.info(":::::::::::::::::::closing the conn");
                con.disconnect();
            }
        }

        return paymentResponse;
    }

    public VerifyPayment verifyPayment(String accessCode, String baseUrl, String endpoint, String token, String private_key) throws SocketTimeoutException, IOException, Exception {
        log.info("::::::::::::::::::::::::::calling verify payment endpoint");
        log.info(":::::::::::::::::::::::::::::the accessCode used for peyment verification is: " + accessCode);

        log.info("::::::::::::::::::::::The token and privaet_key are: " + token + " " + private_key);

        VerifyPayment verifyPayment = null;
        HttpURLConnection con = null;
        String inputLine;
        String finalUrl = null;
        String byteString = null;

        try {
            log.info("::::::::::::::::::verify Payment Endpoint >>>> " + endpoint);

            if (endpoint != null) {
                finalUrl = baseUrl + endpoint;

                log.info("::::::::::::::::;Verify Transaction Payment Final Url >>>> " + finalUrl);

                VerifyPaymentRequest verifyPaymentRequest = new VerifyPaymentRequest(accessCode);
                Gson gson = new Gson();
                String jsonStringParameters = gson.toJson(verifyPaymentRequest);

                log.info("::::::::::::::::::JSON REQUEST PARAMETERS:::: " + jsonStringParameters);

                URL url = new URL(finalUrl);
                String USER_AGENT = "Mozilla/5.0";
                con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("PRIVATE-KEY", private_key);
                con.setRequestProperty("TOKEN", token);

                try ( OutputStream os = con.getOutputStream()) {
                    log.info(":::::::::::::::::::::::::: the json request sent for " + jsonStringParameters);
                    byte[] input = jsonStringParameters.getBytes("utf-8");
                    byteString = Arrays.toString(input);
                    log.info(":::::::::::::::::::::::::: the byte String request sent for " + byteString);
                    String answer = new String(input);
                    log.info("::::::::::::::::::::::::::the array of bytes converted to string is: " + answer);
                    os.write(input, 0, input.length);
                    os.flush();
                }

                int responseCode = con.getResponseCode();
                log.info("::::::::::::::::::::::::::Response Code ::: " + responseCode);

                if (responseCode != 200) {
                    ///log.error("Failed : HTTP error code : " + responseCode);
                    log.info("::::::::::::::::::::::::::::Failed : HTTP error code : " + responseCode);
                    throw new ServerException("Failed : HTTP error code : " + responseCode);
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String output = response.toString();
                log.info("::::::::::::::::::::::::::::::::verify Payment Transaction Response >>>> " + output);

                // JSON
                verifyPayment = gson.fromJson(output, VerifyPayment.class);
            }
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        return verifyPayment;
    }

    public String generateToken(String tokenEnv, String uri, String usernameDemo, String passwordDemo,
                                String usernameLive, String passwordLive, Logger logger) {
        String token = "";
        String USERNAME = "";
        String PASSWORD = "";
        String endpoint = "";
        String inputLine;
        TokenResponse tokenResponse = null;
        TokenRequest tokenRequest = null;

        log.info("RECEIVED REQUEST TO GENERATE TOKEN");

        if (tokenEnv.equals("LIVE")) {
            //log.info("Requesting LIVE token access code...");
            endpoint = uri + "live";
            log.info("Token URL :::: " + endpoint);
            tokenRequest = new TokenRequest(usernameLive, passwordLive);
        } else {
            //log.info("Requesting DEMO token access code...");
            endpoint = uri + "demo";
            log.info("Token URL :::: " + endpoint);
            tokenRequest = new TokenRequest(usernameDemo, passwordDemo);
        }

        Gson gson = new Gson();
        String jsonStringParameters = gson.toJson(tokenRequest);
        try {
            // URL url = new URL(httpsUrl);

            // Create a context that doesn�t check certificates.
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager[] trustManager = getTrustManager();
            sslContext.init(null, trustManager, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            URL url = new URL(endpoint);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");

            try ( OutputStream os = con.getOutputStream()) {
                byte[] input = jsonStringParameters.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            log.info("TOKEN ENDPOINT RESPONSE CODE ::::" + responseCode);

            if (responseCode != 200) {
                //log.error("Failed : HTTP error code : " + responseCode);
                log.info("Failed : HTTP error code : " + responseCode);
                throw new ServerException("Failed : HTTP error code : " + responseCode);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String output = response.toString();

            // JSON
            tokenResponse = gson.fromJson(output, TokenResponse.class);
            token = tokenResponse.getToken();
        } catch (Exception e) {
            log.info("Token Error >>>>> " + e.getMessage());
        }

        return token;
    }

    private TrustManager[] getTrustManager() {
        TrustManager[] certs = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String t) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String t) {
            }
        }
        };
        return certs;
    }


}
