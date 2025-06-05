package com.etranzact.vasgate.aedc.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtil {
    public static JsonObject connectionPostParameters = null;

    private static final Logger L = Logger.getLogger(HttpUtil.class);

    private static final String USER_AGENT = "Mozilla/5.0";

    public static String[] sendGet(String url, String accesscode) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Authorization", "bearer " + accesscode);

        int responseCode = con.getResponseCode();
        L.info("Sending 'GET' request to URL : " + url);
        L.info("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuffer response = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String[] resp = {responseCode + "", response.toString()};

        L.info(response.toString());
        return resp;
    }

    public static String[] sendPost(String url, String jsonString, String accessCode) throws Exception {
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + jsonString);
        System.out.println("Post accessCode : " + accessCode);

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
//        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Content-Type", "application/json");
//        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Authorization", "Bearer " + accessCode);
        con.setDoOutput(true);

        System.out.println("Connection Header : " + con.getHeaderFields().toString());
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(jsonString);
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuffer response = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String[] resp = {responseCode + "", response.toString()};
        return resp;
    }

    /**
     * @param con
     * @param data
     * @return HttpURLConnection
     */
    private static HttpsURLConnection setConnectionProperties(HttpsURLConnection con, JsonObject data) {

        if (data != null) {
            for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().getAsString();
                if (!key.equalsIgnoreCase("url")) {
                    con.setRequestProperty(key, value);

                    if (key.equalsIgnoreCase("timeout")) con.setConnectTimeout(Integer.parseInt(value));
                    if (key.equalsIgnoreCase("readTimeout")) con.setReadTimeout(Integer.parseInt(value));
                    if (key.equalsIgnoreCase("doOutput")) con.setDoOutput(entry.getValue().getAsBoolean());
                }
            }
        }
        return con;
    }

    /**
     * @param endpoint
     * @param jsonString
     * @return String[]
     * @throws Exception
     */
    public static String[] doPost(String endpoint, String jsonString) throws Exception {
        L.info("\nSending 'POST' request to URL : " + endpoint);
        L.info("Post parameters : " + jsonString);

        URL url = new URL(endpoint);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        L.info("connectionPostParameters :: " + connectionPostParameters);
        con = setConnectionProperties(con, connectionPostParameters);
        con.setDoOutput(true);


        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonString.getBytes("utf-8");
            os.write(input, 0, input.length);
            os.flush();
        }

        int responseCode = con.getResponseCode();
        L.info("Response Code : {}" + responseCode);

        InputStream stream = responseCode == 200 ? con.getInputStream() : con.getErrorStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String[] resp = {responseCode + "", response.toString()};
        return resp;
    }


}
