package com.lemonpay.lemonpayvas.electricity.eedc.util;

//import javax.net.ssl.HostnameVerifier;
import com.lemonpay.lemonpayvas.electricity.eedc.enums.Response;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HTTPUtils {

    private static final Logger LOGGER = Logger.getLogger(HTTPUtils.class.getName());
    private static final String REST_CONTENT_TYPE = "application/json";
    private static final String SOAP_CONTENT_TYPE = "application/xml";
    private static final String REST_CONTENT_FORM_TYPE = "application/x-www-form-urlencoded";
    private static final String HTTP_METHOD_GET = "GET";
    private static final String HTTP_METHOD_POST = "POST";
    private static final String HTTP_METHOD_PUT = "PUT";
    private static final String HTTP_METHOD_DELETE = "DELETE";
    private static final String HTTP_METHOD_PATCH = "PATCH";
    private static final int TIMEOUT_SEC = 20;

    public static String[] doAllREQUEST(int requestMethod, int contentType, String theURL, String jsonParams, Map<String, String> headers, int timeoutSec, int ssl) {
        String httpMethod = "", content = "";
        String[] outcome = {"", ""};
        boolean enableSSL = false;
        switch (requestMethod) {
            case 1:
                httpMethod = HTTP_METHOD_GET;
                break;
            case 2:
                httpMethod = HTTP_METHOD_POST;
                break;
            case 3:
                httpMethod = HTTP_METHOD_PUT;
                break;
            case 4:
                httpMethod = HTTP_METHOD_DELETE;
                break;
            case 5:
                httpMethod = HTTP_METHOD_PATCH;
                break;
            default:
                httpMethod = HTTP_METHOD_GET;
                break;

        }

        switch (contentType) {
            case 1:
                content = REST_CONTENT_TYPE;
                break;
            case 2:
                content = SOAP_CONTENT_TYPE;
                break;
            case 3:
                content = REST_CONTENT_FORM_TYPE;
                break;
            default:
                content = REST_CONTENT_TYPE;
                break;

        }

        if (timeoutSec == 0) {
            timeoutSec = TIMEOUT_SEC;
        }
        if (ssl == 1) {
            enableSSL = true;
        }

        if (httpMethod.equalsIgnoreCase(HTTP_METHOD_GET)) {
            outcome = doGETRequest(httpMethod, content, theURL, timeoutSec, enableSSL);
        } else if (httpMethod.equalsIgnoreCase(HTTP_METHOD_POST)) {
            outcome = doPOSTRequest(httpMethod, content, theURL, jsonParams, headers, timeoutSec, enableSSL);
        } else if (httpMethod.equalsIgnoreCase(HTTP_METHOD_PATCH)) {
                outcome = doPATCHRequest(httpMethod, content, theURL, jsonParams, headers, timeoutSec, enableSSL);
            }


            // System.out.println("=====> Response :::: " + Arrays.toString(outcome) + " \n=====>FROM URL " + theURL + " \n=====> Params " + jsonParams);

            return outcome;

        }

        public static String[] doGETRequest (String methodType, String contentType, String theURL,int timeoutSec,
        boolean enableSSL){
            //StringBuilder sbr = new StringBuilder();

            String[] outcome = {"", ""};
            try {
                theURL = theURL.trim();
                if (enableSSL) {
                    disableSSLVerification();
                }
                HttpURLConnection connection = null;
                String readLine = null;

                URL url = new URL(theURL);
                //LOGGER.log(Level.INFO, "Calling get URL :::: ", url);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(methodType);
                connection.setRequestProperty("Content-Type", contentType);
                connection.setConnectTimeout(timeoutSec * 1000);
                connection.connect();
                int responseCode = connection.getResponseCode();
                outcome[0] = responseCode + "";

                switch (responseCode) {
                    case HttpURLConnection.HTTP_OK:
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuffer response = new StringBuffer();
                        while ((readLine = in.readLine()) != null) {
                            response.append(readLine);
                        }
                        in.close();
                        // print result
                        outcome[1] = response.toString();
                        //LOGGER.log(Level.INFO, "output :::: ", outcome + " - " + theURL);
                        break;
                    case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                        outcome[1] = new StringBuilder().append("ErrorCode:: ").append(HttpURLConnection.HTTP_GATEWAY_TIMEOUT).append("~errorMessage:: HTTP_GATEWAY_TIMEOUTURL:: ").toString();
                        //LOGGER.log(Level.INFO, "output :::: ", outcome + " - " + theURL);
                        break;
                    case HttpURLConnection.HTTP_UNAVAILABLE:
                        outcome[1] = new StringBuilder().append("ErrorCode:: ").append(HttpURLConnection.HTTP_UNAVAILABLE).append("~errorMessage:: HTTP_UNAVAILABLE").toString();
                        //LOGGER.log(Level.INFO, "output :::: ", outcome + " - " + theURL);
                        break;// retry, server is unstable
                    case HttpURLConnection.HTTP_BAD_REQUEST:
                        outcome[1] = new StringBuilder().append("ErrorCode:: ").append(HttpURLConnection.HTTP_BAD_REQUEST).append("~errorMessage:: HTTP_BAD_REQUEST").toString();
                        //LOGGER.log(Level.INFO, "output :::: ", outcome + " - " + theURL);
                        break;// retry, server is unstable
                    case HttpURLConnection.HTTP_NO_CONTENT:
                        outcome[1] = new StringBuilder().append("ErrorCode:: ").append(HttpURLConnection.HTTP_NO_CONTENT).append("~errorMessage:: HTTP_NO_CONTENT").toString();
                        break;// retry, server is unstable
                    default:
                        outcome[1] = new StringBuilder().append("ErrorCode:: ").append(responseCode).append("~errorMessage:: HTTP_ERROR_").append(responseCode).toString();
                        //LOGGER.log(Level.INFO, "output :::: ", outcome + " - " + theURL);
                        break; // abort
                }

                connection.disconnect();
            } catch (Exception x) {
                LOGGER.log(Level.SEVERE, "Exception occur :::: ", x.getMessage());
                outcome[1] = new StringBuilder().append("Error:: ").append(x.getMessage()).toString();
                LOGGER.log(Level.INFO, "output :::: ", Arrays.toString(outcome) + " - " + theURL);
            }
            return outcome;
        }


        public static String[] doPATCHRequest (String methodType, String contentType, String theURL, String
        jsonParams, Map < String, String > headers,int timeoutSec, boolean enableSSL){
            String[] outcome = {"", ""};
            try {
                theURL = theURL.trim();
                if (enableSSL) {
                    disableSSLVerification();
                }
                URL obj = new URL(theURL);
                HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
                postConnection.setRequestMethod("POST");
                postConnection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
                postConnection.setConnectTimeout(timeoutSec * 1000);
                if (headers != null) {
                    //LOGGER.log(Level.INFO, " headers ::::: {0}", headers);
                    headers.entrySet().forEach((entry) -> {
                        postConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
                    });
                }
                postConnection.setRequestProperty("Content-Type", contentType);
                postConnection.setDoOutput(true);
                OutputStream os = postConnection.getOutputStream();
                os.write(jsonParams.getBytes());
                os.flush();
                os.close();
                int responseCode = postConnection.getResponseCode();
                outcome[0] = responseCode + "";

                LOGGER.log(Level.INFO, "ResponseCode {0}", responseCode);

                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    LOGGER.log(Level.INFO, " OUTPUT STREAM :{0}", response.toString());
                    outcome[1] = response.toString();
                } catch (Exception ex) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getErrorStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    LOGGER.log(Level.INFO, " ERROR STREAM :{0}", response.toString());
                    outcome[1] = response.toString();
                }
            } catch (IOException x) {
                x.printStackTrace();
                LOGGER.log(Level.SEVERE, "Exception occur :::: ", x);
            }
            return outcome;
        }

        public static String[] doPOSTRequest (String methodType, String contentType, String theURL, String
        jsonParams, Map < String, String > headers,int timeoutSec, boolean enableSSL){
            String[] outcome = {"", ""};
            try {
                theURL = theURL.trim();
                if (enableSSL) {
                    disableSSLVerification();
                }
                URL obj = new URL(theURL);
                HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
                postConnection.setRequestMethod(methodType);
                postConnection.setConnectTimeout(timeoutSec * 1000);
                if (headers != null) {
                    //LOGGER.log(Level.INFO, " headers ::::: {0}", headers);
                    headers.entrySet().forEach((entry) -> {
                        postConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
                    });
                }
                postConnection.setRequestProperty("Content-Type", contentType);
                postConnection.setDoOutput(true);
                OutputStream os = postConnection.getOutputStream();
                os.write(jsonParams.getBytes());
                os.flush();
                os.close();
                int responseCode = postConnection.getResponseCode();
                outcome[0] = responseCode + "";

                LOGGER.log(Level.INFO, "ResponseCode {0}", responseCode);

                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    LOGGER.log(Level.INFO, " OUTPUT STREAM :{0}", response.toString());
                    outcome[1] = response.toString();
                }catch (SocketException sc){
                    outcome[0] ="408";
                    outcome[1] = "Timeout";

                }

                catch (Exception ex) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getErrorStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    LOGGER.log(Level.INFO, " ERROR STREAM :{0}", response.toString());
                    outcome[0] = Response.ERROR_OCCURED.code;
                    outcome[1] = Response.ERROR_OCCURED.toString();
                }
            } catch (IOException x) {
                x.printStackTrace();
                LOGGER.log(Level.SEVERE, "Exception occur :::: ", x);
                //LOGGER.log(Level.INFO, " ERROR STREAM :{0}", response.toString());
                outcome[0] = Response.ERROR_OCCURED.code;
                outcome[1] = Response.ERROR_OCCURED.toString();
            }
            return outcome;
        }


        // Method used for bypassing SSL verification
        public static void disableSSLVerification () {

            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

            }};

            SSLContext sc = null;
            try {
                sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());

                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                javax.net.ssl.HostnameVerifier allHostsValid = new javax.net.ssl.HostnameVerifier() {

                    public boolean verify(String host, SSLSession session) {
                        return true;
                    }

                    public void check(String host, SSLSocket ssl) throws IOException {
                        check(new String[]{host}, ssl);
                    }

                    public void check(String host, X509Certificate cert) throws SSLException {
                        check(new String[]{host}, cert);
                    }

                    public void check(String host, String[] cns, String[] subjectAlts) throws SSLException {
                        check(new String[]{host}, cns, subjectAlts);
                    }

                    public void check(String[] hosts, SSLSocket ssl) throws IOException {
                        if (hosts == null) {
                            throw new NullPointerException("host to verify is null");
                        }

                        SSLSession session = ssl.getSession();
                        if (session == null) {
                            // In our experience this only happens under IBM 1.4.x when
                            // spurious (unrelated) certificates show up in the server'
                            // chain. Hopefully this will unearth the real problem:
                            InputStream in = ssl.getInputStream();
                            in.available();
                            /*
                             * If you're looking at the 2 lines of code above because you're running into a
                             * problem, you probably have two options:
                             *
                             * #1. Clean up the certificate chain that your server is presenting (e.g. edit
                             * "/etc/apache2/server.crt" or wherever it is your server's certificate chain
                             * is defined).
                             *
                             * OR
                             *
                             * #2. Upgrade to an IBM 1.5.x or greater JVM, or switch to a non-IBM JVM.
                             */

                            // If ssl.getInputStream().available() didn't cause an
                            // exception, maybe at least now the session is available?
                            session = ssl.getSession();
                            if (session == null) {
                                // If it's still null, probably a startHandshake() will
                                // unearth the real problem.
                                ssl.startHandshake();

                                // Okay, if we still haven't managed to cause an exception,
                                // might as well go for the NPE. Or maybe we're okay now?
                                session = ssl.getSession();
                            }
                        }
                        Certificate[] certs;
                        try {
                            certs = session.getPeerCertificates();
                        } catch (SSLPeerUnverifiedException spue) {
                            InputStream in = ssl.getInputStream();
                            in.available();
                            // Didn't trigger anything interesting? Okay, just throw
                            // original.
                            throw spue;
                        }
                        X509Certificate x509 = (X509Certificate) certs[0];
                        check(hosts, x509);
                    }

                    public void check(String[] hosts, X509Certificate cert) throws SSLException {
                        String[] cns = HostnameVerifier.Certificates.getCNs(cert);
                        String[] subjectAlts = HostnameVerifier.Certificates.getDNSSubjectAlts(cert);
                        check(hosts, cns, subjectAlts);
                    }

                    public void check(String[] hosts, String[] cns, String[] subjectAlts) throws SSLException {
                        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                        // methods, choose Tools |
                        // Templates.
                    }
                };

                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

    }
