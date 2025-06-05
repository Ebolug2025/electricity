package com.etranzact.vasgate.aedc.service;

import com.etranzact.vasgate.aedc.dto.*;
import com.etranzact.vasgate.aedc.dto.SearchCriterion;
import com.etranzact.vasgate.aedc.model.Customer;
import com.etranzact.vasgate.aedc.model.UnitTopUp;
import com.etranzact.vasgate.aedc.dto.CustomerRequest;
import com.etranzact.vasgate.aedc.util.HTTPUtils;
import com.etranzact.vasgate.aedc.util.RedisService;
import com.etranzact.vasgate.aedc.util.Session;
import com.etranzact.vasgate.aedc.util.SessionFactory;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.*;

@Slf4j
public class AEDCService {

    @Autowired
    private RedisService redisService;
    private String prepaidUsername;
    private String postpaidUsername;
    private String prepaidPassword;
    private String postpaidPassword;
    private String grantType;
    //private Logger logger;
    private String baseUrl;
    private String clientSecret;

    private String redisUrl;

    private static Map<String, String> channelMap = null;



    static {
        if (channelMap == null) {
            channelMap = new HashMap<>();
            channelMap.put("02", "USSD-12");
            channelMap.put("03", "POS-1");
            channelMap.put("05", "Bank Branch-3");
            channelMap.put("others", "Internet-5");
            channelMap.put("cash-office", "CashOffice-2");
        }
    }

    public AEDCService(String prepaidUsername, String postpaidUsername, String prepaidPassword, String postpaidPassword, String grantType, String baseUrl, String clientSecret, Logger logger, String redisUrl) {
        this.prepaidUsername = prepaidUsername;
        this.postpaidUsername = postpaidUsername;
        this.prepaidPassword = prepaidPassword;
        this.postpaidPassword = postpaidPassword;
        this.grantType = grantType;
        this.baseUrl = baseUrl;
        this.clientSecret = clientSecret;
       // this.logger = logger;
        this.redisUrl = redisUrl;
    }

    public AEDCService(String prepaidUsername, String postpaidUsername, String prepaidPassword, String postpaidPassword, String grantType, String baseUrl, String clientSecret, String redisUrl) {
    }

    public TokenResponse getAccessToken(String username, String password) {
        Map<String, String> conProperties = new HashMap<>();
        log.info("getAccessToken Method() username :: " + username + " :: " + password);

        String tokenRequest = "grant_type=" + this.grantType + "&username=" + username + "&password=" + password;
        conProperties.put("Authorization", "Basic " + clientSecret);
//        HttpUtil.connectionPostParameters = conProperties;
        String resp[];
        TokenResponse tokenResponse = new TokenResponse();

        try {
            resp = HTTPUtils.doAllREQUEST(2, 3, this.baseUrl + "token",
                    tokenRequest, conProperties, 0, 1);

            log.info("Access Token Response Code :: " + resp[0]);
            log.info("Access Token Response Data :: " + resp[1]);
            tokenResponse = new Gson().fromJson(resp[1], TokenResponse.class);

            log.info(tokenResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("tokenResponse :: " + tokenResponse);
        return tokenResponse;
    }

    public String[] validateMeter(CustomerRequest customerRequest, String meterType) throws Exception {
        String username = getUserName(meterType);
        String password = getPassword(meterType);

        log.info("validateMeter() username :: " + username + " password :: " + password);
        TokenResponse tokenResponse = getAccessToken(username, password);
        log.info("tokenResponse " + tokenResponse.toString());
        String strToken = new Gson().toJson(tokenResponse);
        log.info("strToken " + strToken);

        String[] resp = makeNestedCustomerInformationRequest(customerRequest, tokenResponse,
                meterType, strToken);

        return resp;
    }

    private String[] makeNestedCustomerInformationRequest(CustomerRequest customerRequest, TokenResponse tokenResponse,
                                                          String meterType, String strToken) {
        Session session = SessionFactory.getSingleton();
        List<SearchCriterion> searchCriterionList = session.getSearchCriteria();
        int counter = 0;
        counter += 1;
        log.info("=== START :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY METER NUMBER === " + customerRequest.getValue());
        SearchCriterion searchCriterion = searchCriterionList.get(0); // Search by meter number
        customerRequest.setCodType(searchCriterion.getCodType());
        String[] resp = getCustomerInformation(tokenResponse, customerRequest, meterType, strToken);
        log.info("=== END :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY METER NUMBER === " + customerRequest.getValue());

        if (!resp[0].equals("200")) {
            counter += 1;
            log.info("=== START :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY ACCOUNT NUMBER === " + customerRequest.getValue());
            searchCriterion = searchCriterionList.get(1); // Search by account number
            customerRequest.setCodType(searchCriterion.getCodType());
            resp = getCustomerInformation(tokenResponse, customerRequest, meterType, strToken);
            log.info("=== END :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY ACCOUNT NUMBER === " + customerRequest.getValue());
            if (!resp[0].equals("200")) {
                counter += 1;
                log.info("=== START :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY CUSTOMER IDENTIFICATION === " + customerRequest.getValue());
                searchCriterion = searchCriterionList.get(2); // Search by customer identification number
                customerRequest.setCodType(searchCriterion.getCodType());
                resp = getCustomerInformation(tokenResponse, customerRequest, meterType, strToken);
                log.info("=== END :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY CUSTOMER IDENTIFICATION === " + customerRequest.getValue());
                if (!resp[0].equals("200")) {
                    counter += 1;
                    log.info("=== START :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY DRIVER LICENSE=== " + customerRequest.getValue());
                    searchCriterion = searchCriterionList.get(3); // Search by driver license
                    customerRequest.setCodType(searchCriterion.getCodType());
                    resp = getCustomerInformation(tokenResponse, customerRequest, meterType, strToken);
                    log.info("=== END :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY DRIVER LICENSE === " + customerRequest.getValue());
                    if (!resp[0].equals("200")) {
                        counter += 1;
                        log.info("=== START :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY OLD ACCOUNT === " + customerRequest.getValue());
                        searchCriterion = searchCriterionList.get(4); // Search by old account
                        customerRequest.setCodType(searchCriterion.getCodType());
                        resp = getCustomerInformation(tokenResponse, customerRequest, meterType, strToken);
                        log.info("=== END :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY OLD ACCOUNT === " + customerRequest.getValue());
                    }
                }
            }
        }

        return resp;
    }

    private String[] getCustomerInformation(TokenResponse tokenResponse, CustomerRequest customerRequest, String meterType, String strToken) {
        String[] resp = {"", ""};
        String url = this.baseUrl + "venMeter/1.0.1/";

        if (strToken.contains("access_token")) {
            String postRequest = new Gson().toJson(customerRequest);

            this.log.info("CALLING ABUJA " + meterType.toUpperCase() + " VENDING GATEWAY FOR METER VALIDATION ");
            this.log.info(meterType.toUpperCase() + " REQUEST " + postRequest);
            this.log.info("CALLED URL :: " + url);

            Map<String, String> header = new HashMap<>();
            header.put("Authorization", "Bearer " + tokenResponse.getAccess_token());

            this.log.info("Authorization header :: " + header);
            log.info("verification url " + url);
            resp = HTTPUtils.doAllREQUEST(2, 1, url,
                    postRequest, header, 0, 1);

            this.log.info("RESPONSE FROM ABUJA " + meterType.toUpperCase() + " VENDING GATEWAY :: " + resp[1]);
        }

        return resp;
    }

    public String[] calculatePayment(TokenResponse tokenResponse, String amount, String username, String meterSerial) throws Exception {

        String[] resp = {"", ""};

        String[] userParts = getVendorIdAndCodUser(username);
        String idVendor = userParts[0];
        String codUser = userParts[1];

        String strToken = new Gson().toJson(tokenResponse);
        log.info("strToken " + strToken);

        if (strToken.contains("access_token")) {
            String accessToken = tokenResponse.getAccess_token();

            PaymentValueRequest pvr = new PaymentValueRequest();
            pvr.setCodUser(codUser).setIdVendor(idVendor)
                    .setDebtPayment(Double.parseDouble(formatAmount("0.00")))
                    .setTotalPayment(Double.parseDouble(formatAmount(amount)))
                    .setMeterSerial(meterSerial);


           // RedisService redisService = new RedisService(redisUrl);

            // Session session = SessionFactory.getSingleton();
            //Customer[] customers = new Gson().fromJson( redisService.getInstance().get(meterSerial), Customer[].class);
            Customer[] customers = new Gson().fromJson((String) redisService.getInstance().opsForValue().get(meterSerial), Customer[].class);
            if (customers.length > 0) {
                Customer customer = customers[0];
                pvr.setAccount(customer.getAccount());
            }


            String postRequest = new Gson().toJson(pvr);

            Map<String, String> header = new HashMap<>();
            header.put("Authorization", "Bearer " + accessToken);

            String url = this.baseUrl + "venPayment/1.0.1/calculatePayment";
            resp = HTTPUtils.doAllREQUEST(2, 1, url,
                    postRequest, header, 0, 1);

            log.info("Payment Value Response :: " + Arrays.toString(resp));
        }


        return resp;
    }

    private String[] makePayment(String accessToken, String idVendor, String codUser, String meterSerial,
                                 String amount, String accountOrMeterNo, String mobile, String email,
                                 String uniqueReference, PaymentValueResponse paymentValueResponse, String paymentChannel, int channel) throws Exception {

        //  Session session = SessionFactory.getSingleton();
       // RedisService redisService = new RedisService(redisUrl);
        log.info("MakePayment() Customers From Redis :: " +  redisService.getInstance().opsForValue().get(meterSerial));
        //Customer[] customers = new Gson().fromJson(redisService.getInstance().get(meterSerial), Customer[].class);
        Customer[] customers = new Gson().fromJson((String) redisService.getInstance().opsForValue().get(accountOrMeterNo), Customer[].class);
        Customer customer = customers[0];

        PaymentRequest paymentRequest = new PaymentRequest();
        // int channel = 0;
        //  String channelKey = uniqueReference.substring(0, 2);
//        String channel = channelMap.get(channelKey);
//        if (channel == null) channel = channelMap.get("others");
//        if (!paymentChannel.startsWith("0") && channelKey.equals("09")) {
//            channel = Integer.parseInt(paymentChannel);
//        } else {
//            if (channelKey.equals("01") || channelKey.equals("09")) {
//                //channel = 5;                 Kindly change channel 5 (Internet Customer by Self) to Channel 9 (Vending Kiosks).
//            //    channel = 9;  to change channel 9 (Vending Kiosks) to Channel 15 (mobile cashiering)
//                channel = 15;
//            } else if (channelKey.equals("02")) {
//                if (uniqueReference.contains("02USD")) {
//                   // channel = 7;//was asked to change to 1 for ussd trans
//                    channel = 1;
//                } else if (uniqueReference.contains("02POS")) {
////                    channel = 1;
//                    channel = 15;
//
//                } else {
//                    //channel = 7;
//                    channel = 15;
//                }
//            } else if (channelKey.equals("03")) {
//                //channel = 1;//15
//                channel = 15;
//            } else if (channelKey.equals("11")) {
//                //channel = 7;
//                channel = 15;
//            } else if (channelKey.equals("05")) {
//                //Kindly change channel 5 (Internet Customer by Self) to Channel 9 (Vending Kiosks).
//               // channel = 5;
//                //  channel = 9;to change channel 9 (Vending Kiosks) to Channel 15 (mobile cashiering)
//                channel = 15;
//            } else {
//               // channel = 5;               Kindly change channel 5 (Internet Customer by Self) to Channel 9 (Vending Kiosks).
//                //  channel = 9;to change channel 9 (Vending Kiosks) to Channel 15 (mobile cashiering)
//                channel = 15;
//            }
//        }


        //String channel = channelMap.get("cash-office");
        // int channelCode = Integer.parseInt(channel.split("-")[1]);

        paymentRequest.setIdVendor(idVendor).setCodUser(codUser).setMeterSerial(meterSerial)
                .setTotalPayment(Double.parseDouble(formatAmount(amount)))
                .setDebtPayment(paymentValueResponse.getDebtPayment())
                .setAccount(paymentValueResponse.getAccount()).setTariffDescription(paymentValueResponse.getTariffDescription())
                .setPercentageDebt(paymentValueResponse.getPercentageDebt())
                .setAccountBalance(paymentValueResponse.getAccountBalance())
                .setUnitsPayment(paymentValueResponse.getUnitsPayment())
                .setUnits(paymentValueResponse.getUnits())
                .setUnitsType(paymentValueResponse.getUnitsType())
                .setComment("Payment Received").setRequestID(uniqueReference)
                //.setChannel(channelCode)
                .setChannel(channel)
                .setAreaCode(0) //TODO: find out the right value
                .setServiceCode(0) //TODO: find out the right value
                .setPhoneNo(mobile)
                .setEmail(email);

        log.info("Make Payment Request:: " + paymentRequest);
        String postRequest = new Gson().toJson(paymentRequest);
        log.info("Make Payment Request :: postRequest :: " + postRequest);


        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + accessToken);

        String url = this.baseUrl + "venPayment/1.0.1/makePayment";

        log.info("URL :: " + url);
        log.info("header :: " + header);

        String[] resp = HTTPUtils.doAllREQUEST(2, 1, url,
                postRequest, header, 0, 1);

        log.info("Payment Response FROM Abuja :: " + Arrays.toString(resp));

        return resp;
    }

    private String getUserName(String meterType) {
        return meterType.equals("prepaid") ? prepaidUsername : postpaidUsername;
    }

    private String getPassword(String meterType) {
        return meterType.equals("prepaid") ? prepaidPassword : postpaidPassword;
    }

    private String[] getVendorIdAndCodUser(String username) {
        return username.split("#");
    }

    public String[] getUsernamePassword(String meterType) {

        String username = getUserName(meterType);
        String password = getPassword(meterType);
        String[] credentials = {username, password};

        return credentials;
    }

    public String[] vendPin(String meter, String amount, String uniqueReference, String mobile, String email,
                            String meterType, Customer customer, TokenResponse tokenResponse, String username, String[] calculatePaymentResponse, String paymentChannel, int channel) throws Exception {

        String[] paymentResponse = {"", ""};
        String successString = "";

        String[] userParts = getVendorIdAndCodUser(username);
        String idVendor = userParts[0];
        String codUser = userParts[1];

        String strToken = new Gson().toJson(tokenResponse);
        log.info("strToken " + strToken);

        if (strToken.contains("access_token")) {
            PaymentValueResponse paymentValueResponse = null;

            if (calculatePaymentResponse.length > 0 && calculatePaymentResponse[0].equals("200")) {
                paymentValueResponse = new Gson().fromJson(calculatePaymentResponse[1], PaymentValueResponse.class);

                this.log.info("CALLING ABUJA VENDING GATEWAY FOR TOKEN VENDING :: " + uniqueReference);

                paymentResponse = postPayment(tokenResponse, idVendor, codUser, customer
                        , amount, meter, mobile, email, uniqueReference, paymentValueResponse, paymentChannel, channel);
            } else {
                if (calculatePaymentResponse.length > 0 && calculatePaymentResponse[0].equals("204")
                        && meterType.equals("postpaid")) {
                    paymentResponse = postPayment(tokenResponse, idVendor, codUser, customer
                            , amount, meter, mobile, email, uniqueReference, paymentValueResponse, paymentChannel, channel);
                } else {
                    paymentResponse = calculatePaymentResponse;
                }
            }
        }

        return paymentResponse;
    }

    private String[] postPayment(TokenResponse tokenResponse, String idVendor, String codUser, Customer customer
            , String amount, String meter, String mobile, String email, String uniqueReference, PaymentValueResponse paymentValueResponse, String paymentChannel, int channel) throws Exception {
        String[] paymentResponse = makePayment(tokenResponse.getAccess_token(), idVendor, codUser, customer.getMeterSerial(), amount,
                meter, mobile, email, uniqueReference, paymentValueResponse, paymentChannel, channel);

        this.log.info("RESPONSE FROM ABUJA VENDING GATEWAY :: " + Arrays.toString(paymentResponse));

        return paymentResponse;
    }

    public String buildErrorMessage(FailResponse failResponse) {
        String message = "";
        if (failResponse.getMsgDeveloper().equals(failResponse.getMsgUser())) {
            message = failResponse.getMsgUser();
        } else {
            message = failResponse.getMsgUser() + " - " + failResponse.getMsgDeveloper();
        }
        return message;
    }

    private String[] fetchPaymentDetail(TokenResponse tokenResponse, String idVendor, String codUser, String uniqueReference) {
        // fetch payment information after successful payment request ...
        PaymentDetailsRequest paymentDetailsRequest = buildPaymentRequest(idVendor, codUser, uniqueReference);
        String pdrJsonRequest = new Gson().toJson(paymentDetailsRequest);
        // calling to fetch payment details ...

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + tokenResponse.getAccess_token());
        String url = this.baseUrl + "venPayment/1.0.1/paymentInfo";

        String[] resp = HTTPUtils.doAllREQUEST(2, 1, url,
                pdrJsonRequest, header, 0, 1);

        return resp;
    }

    public PaymentDetailsRequest buildPaymentRequest(String idVendor, String codUser, String uniqueRef) {
        PaymentDetailsRequest paymentDetailsRequest = new PaymentDetailsRequest();
//        paymentDetailsRequest.setTransactionId(uniqueRef)
//                .setCodUser(codUser)
//                .setIdVendor(idVendor);

        return paymentDetailsRequest;
    }


    public String[] reQueryPrepaidTransaction(PaymentDetailsRequest paymentDetailsRequest, String meterType) {

        String username = getUserName(meterType);
        String password = getPassword(meterType);

        TokenResponse tokenResponse = getAccessToken(username, password);
        log.info("tokenResponse " + tokenResponse.toString());
        String strToken = new Gson().toJson(tokenResponse);
        log.info("strToken " + strToken);

        String[] paymentDetailResponse = {};
        if (strToken.contains("access_token")) {
            String pdrJsonRequest = new Gson().toJson(paymentDetailsRequest);

            Map<String, String> header = new HashMap<>();
            header.put("Authorization", "Bearer " + tokenResponse.getAccess_token());
            String url = this.baseUrl + "venPayment/1.0.1/paymentInfo";

            paymentDetailResponse = HTTPUtils.doAllREQUEST(2, 1, url,
                    pdrJsonRequest, header, 0, 1);
        }

        return paymentDetailResponse;
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

    public String retrieveVAT(PaymentResponse paymentDetailsResponse) {
        String vat = "";

        if (paymentDetailsResponse.getUnitsTopUp().size() > 0) {
            for (UnitTopUp unitTopUp : paymentDetailsResponse.getUnitsTopUp()) {
                if (unitTopUp.getConceptName().equalsIgnoreCase("vat")) {
                    vat = unitTopUp.getAmount() + "";
                    break;
                }
            }
        }

        return vat;
    }

    private String formatAmount(String amount) {
        Formatter formatter = new Formatter();
        formatter.format("%.2f", Double.parseDouble(amount));
        return formatter.toString();
    }
}
