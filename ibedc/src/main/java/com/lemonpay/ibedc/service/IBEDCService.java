package com.lemonpay.ibedc.service;

import com.google.gson.Gson;
import com.lemonpay.ibedc.dto.request.PaymentRequest;
import com.lemonpay.ibedc.dto.response.PaymentResponse;
import com.lemonpay.ibedc.dto.response.QueryResponse;
import lombok.RequiredArgsConstructor;
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

@Slf4j
@RequiredArgsConstructor
public class IBEDCService {
    public QueryResponse customerQuery(String meter, String action, String baseUrl, String endpoint, String apiKey, String publicKey) throws SocketTimeoutException, IOException, Exception {
        log.info(":::::::::::::::::::::IN " + action + " ACCOUNT VERIFICATION METHOD");
        //log.info("::::::::::::::::::::::The token and privaet_key are: " + token + " " + publicKey);
        QueryResponse customerResponse = null;
        HttpURLConnection con = null;
        String finalurl = null;

        String inputLine;
        try {
            log.info(":::::::::::::::::::::" + action + " Account Verification Endpoint >>>> " + endpoint);

            if (endpoint != null) {

                finalurl = baseUrl + endpoint;
                String USER_AGENT = "Mozilla/5.0";
                log.info(":::::::::::::::::" + action + " Account Verification Final Url >>>> " + finalurl);

                URL url = new URL(finalurl);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                con.setRequestMethod("POST");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("public-key", publicKey);
                con.setRequestProperty("api-key", apiKey);
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

    public PaymentResponse customPayment( String request_id, String serviceId, String meterNumber, String meterType, Double amount, String mobile, String baseUrl, String endpoint, String secretKey) throws SocketTimeoutException, IOException, Exception {
        log.info(":::::::::::::::::::::::IN" + meterType + "TRANSACTION PAYMENT METHOD...");


        log.info(":::::::::::::::::::::IN "  + meterType + " ACCOUNT VERIFICATION METHOD");

        PaymentResponse paymentResponse = null;
        HttpURLConnection con = null;
        String inputLine;
        String finalUrl = null;

        try {
            log.info( meterType + " Transaction Payment Endpoint >>>> " + endpoint);

            if (endpoint != null) {
                finalUrl = baseUrl + endpoint;

                log.info(meterType + " Transaction Payment Final Url >>>> " + finalUrl);

                PaymentRequest paymentRequest = new PaymentRequest(request_id,serviceId,meterNumber,meterType,amount,mobile);
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
                con.setRequestProperty("PRIVATE-KEY", secretKey);
                con.setRequestProperty("User-Agent", USER_AGENT);
                //con.setRequestProperty("TOKEN", token);

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
