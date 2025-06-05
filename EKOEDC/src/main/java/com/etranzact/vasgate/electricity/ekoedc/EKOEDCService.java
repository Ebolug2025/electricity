package com.etranzact.vasgate.electricity.ekoedc;

import com.etranzact.vasgate.electricity.ekoedc.Domain.Dto.SearchCriterion;
import com.etranzact.vasgate.electricity.ekoedc.Domain.Model.Customer;
import com.etranzact.vasgate.electricity.ekoedc.Domain.Request.*;
import com.etranzact.vasgate.electricity.ekoedc.Domain.Response.*;
import com.etranzact.vasgate.electricity.ekoedc.Utils.HTTPUtils;
import com.etranzact.vasgate.electricity.ekoedc.Utils.Session;
import com.etranzact.vasgate.electricity.ekoedc.Utils.SessionFactory;
import com.etranzact.vasgate.electricity.phcnnode.PHCNNode;
import com.etranzact.vasgate.electricity.phcnnode.dto.*;
import com.etranzact.vasgate.electricity.phcnnode.dto.ElectricityReQueryRequest;
import com.etranzact.vasgate.electricity.redisutility.redisservice.NewVasgateRedisService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.*;


@Service
@Component("phcneko")
@Slf4j
@JsonIgnoreProperties
public class EKOEDCService extends PHCNNode {

    @Value("${EKOEDC_GRANT_TYPE}")
    private String grantType;

    @Value("${EKOEDC_PREPAID_USERNAME}")
    private String prepaidUsername;

    @Value("${EKOEDC_POSTPAID_USERNAME}")
    private String postpaidUsername;

    @Value("${EKOEDC_PREPAID_PASSWORD}")
    private String prepaidPassword;

    @Value("${EKOEDC_POSTPAID_PASSWORD}")
    private String postpaidPassword;

    @Value("${EKOEDC_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${EKOEDC_BASEURL}")
    private String baseUrl;


    @Value("${EKOEDC_PING_METER_NUMBER}")
    private String pingAccountNumber;

    @Value("${EKO_PING_MOBILE}")
    private String pingMobile;

    @Autowired
    NewVasgateRedisService redisService;
//

//    private String redisUrl;


    private String getUserName(String meterType) {
        return meterType.equals("prepaid") ? prepaidUsername : postpaidUsername;
    }

    private String getPassword(String meterType) {
        return meterType.equals("prepaid") ? prepaidPassword : postpaidPassword;
    }

//    @Override
//    public ElectricityQueryResponse query2(ElectricityQueryRequest electricityQueryRequest) {
//        log.info("*******INSIDE EKO query service ************** ");
//        log.info("******EKO query request:::::::::::: " + electricityQueryRequest);
//        ElectricityQueryResponse electricityQueryResponse = new ElectricityQueryResponse();
//        String output;
//        QuerySuccess querySuccess = null;
//        QueryFailed queryFailed = null;
//        Gson gson = new Gson();
//        String disco = "EKO";
//        String vendType = "";
//        String vertical = "ELECTRICITY";
//        Boolean orderId = false;
//
//        electricityQueryResponse.setAccountNumber(electricityQueryRequest.getPayerId());
//        electricityQueryResponse.setRequestType("query");
//        if (electricityQueryRequest.getType().equalsIgnoreCase("1")) {
//            vendType = "PREPAID";
//        } else if (electricityQueryRequest.getType().equalsIgnoreCase("2")) {
//            vendType = "POSTPAID";
//        } else {
//
//            electricityQueryResponse.setResponseDesc("Invalid meter type");
//            electricityQueryResponse.setResponseCode("01");
//            electricityQueryResponse.setErrorCode("01");
//            electricityQueryResponse.setUniqueTransId(electricityQueryRequest.getReference());
//        }
//        try {
//            output = checkMeter(electricityQueryRequest.getPayerId(), disco, vendType, vertical, orderId, buyPowerBaseUrl, buyPower_Public_Key);
//            String[] outcome = output.split("~");
//            String responseCode = outcome[0];
//
//            if (responseCode.equalsIgnoreCase("200")) {
//                querySuccess = gson.fromJson(outcome[1], QuerySuccess.class);
//
//                electricityQueryResponse.setCustomerName(querySuccess.getName());
//                electricityQueryResponse.setCustomerAddress(querySuccess.getAddress());
//                electricityQueryResponse.setUniqueTransId(electricityQueryRequest.getReference());
//                electricityQueryResponse.setResponseCode("00");
//                electricityQueryResponse.setDisco("EKO");
//                electricityQueryResponse.setResponseDesc("Successful");
//                electricityQueryResponse.setErrorCode("00" /*String.valueOf(querySuccess.getResponseCode())*/);
//                electricityQueryResponse.setMinimumPurchase(String.valueOf(querySuccess.getMinVendAmount()));
//                electricityQueryResponse.setMaxPurchase(String.valueOf(querySuccess.getMaxVendAmount()));
//                electricityQueryResponse.setExternalReference(querySuccess.getOrderId());
//                electricityQueryResponse.setCustomerArrears(String.valueOf(querySuccess.getOutstanding()));
//                electricityQueryResponse.setTariff(querySuccess.getTariff());
//                electricityQueryResponse.setTariffClass(querySuccess.getTariffClass());
//                electricityQueryResponse.setTariffDesc(querySuccess.getTariffDesc());
//                electricityQueryResponse.setBusinessUnit("EKO BUSINESS UNIT");
//
//            } else if (responseCode.equalsIgnoreCase("400")) {
//                queryFailed = gson.fromJson(outcome[1], QueryFailed.class);
//
//                electricityQueryResponse.setResponseCode(String.valueOf(queryFailed.getResponseCode()));
//                electricityQueryResponse.setResponseDesc(queryFailed.getMessage());
//                electricityQueryResponse.setErrorCode("400");
//                electricityQueryResponse.setUniqueTransId(electricityQueryRequest.getReference());
//
//            } else {
//                electricityQueryResponse.setResponseCode("01");
//                electricityQueryResponse.setResponseDesc("Query Failed");
//                electricityQueryResponse.setErrorCode("01");
//                electricityQueryResponse.setUniqueTransId(electricityQueryRequest.getReference());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("::::::::::::::EKO error " + e.getMessage());
//            electricityQueryResponse.setResponseDesc("Error occurred; Exception");
//            electricityQueryResponse.setResponseCode("08");
//            electricityQueryResponse.setErrorCode("08");
//            electricityQueryResponse.setUniqueTransId(electricityQueryRequest.getReference());
//
//        }
//
//
//        log.info("================================== the response returned back to phcn node for eko query is: " + gson.toJson(electricityQueryResponse));
//        return electricityQueryResponse;
//    }
//
//    public String checkMeter(String meterNo, String disco, String vendType, String vertical, Boolean orderId, String baseUrl, String publicKey) throws SocketTimeoutException, IOException, Exception {
//        String output = "";
//        HttpURLConnection con = null;
//        String USER_AGENT = "Mozilla/5.0";
//        String inputLine;
//        String queryUrl = baseUrl + "check/meter?meter=" + meterNo + "&disco=" + disco + "&vendType=" + vendType + "&vertical=" + vertical + "&orderId=" + orderId;
//        log.info("::::::::::::::::::::::EKO meter validation URL >>>> " + queryUrl);
//        try {
//            URL url = new URL(queryUrl);
//            con = (HttpURLConnection) url.openConnection();
//            con.setConnectTimeout(10000);
//            con.setRequestMethod("GET");
//            con.setRequestProperty("Authorization", "Bearer " + publicKey);
//
//            int responseCode = con.getResponseCode();
//            log.info("::::::::::::::::::::::::responseCode from BUYPOWER for eko meter validation ::: " + responseCode);
//
//            BufferedReader in;
//            StringBuffer response = new StringBuffer();
//            if (responseCode == 200) {
//                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//
//            } else {
//                in = new BufferedReader(new InputStreamReader(
//                        con.getErrorStream()));
//
//            }
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            output = responseCode + "~" + response.toString();
//            log.info("::::::::::::::::::::::Response from BUYPOWER for EKO meter validation >>>> " + output);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (con != null) {
//                log.info(":::::::::::::::::::closing the conn");
//                con.disconnect();
//            }
//        }
//
//        return output;
//    }

    @Override
    public ElectricityQueryResponse query(ElectricityQueryRequest electricityQueryRequest) {
        log.info("*******INSIDE EKOEDC query MtHD ************** ");
        log.info("******query request:::::::::::: " + electricityQueryRequest);
        String vendType = "";
        double amount = electricityQueryRequest.getAmount();
        String reference = electricityQueryRequest.getReference();
        ElectricityQueryResponse electricityQueryResponse = new ElectricityQueryResponse();

        if (electricityQueryRequest.getPayerId().isEmpty() || electricityQueryRequest.getPayerId() == null) {
            electricityQueryResponse.setResponseCode("06");
            electricityQueryResponse.setResponseDesc("Account cannot be empty");
            electricityQueryResponse.setErrorCode("06");
            return electricityQueryResponse;
        } else if (reference.isEmpty() || reference == null) {
            electricityQueryResponse.setResponseCode("06");
            electricityQueryResponse.setResponseDesc("Reference cannot be empty");
            electricityQueryResponse.setErrorCode("06");
            return electricityQueryResponse;
        } else if (electricityQueryRequest.getType().isEmpty() || electricityQueryRequest.getType() == null) {
            electricityQueryResponse.setResponseCode("06");
            electricityQueryResponse.setResponseDesc("Action cannot be empty");
            electricityQueryResponse.setErrorCode("06");
            return electricityQueryResponse;
        }

        if (electricityQueryRequest.getType().equalsIgnoreCase("1")) {
            vendType = "prepaid";
        } else if (electricityQueryRequest.getType().equalsIgnoreCase("2")) {
            vendType = "postpaid";
        } else {

            electricityQueryResponse.setResponseDesc("Invalid meter type: 1 for prepaid and 2 for postpaid");
            electricityQueryResponse.setResponseCode("06");
            electricityQueryResponse.setErrorCode("06");
            electricityQueryResponse.setUniqueTransId(reference);
            return electricityQueryResponse;
        }

        if (amount <= 0) {
            amount = 2000;
        }

        try {
            CustomerRequest customerRequest = new CustomerRequest();
            PaymentValueResponse paymentValueResponse;
            String strToken;
            TokenResponse tokenResponse = new TokenResponse();
            try {
                tokenResponse = getAccessToken(getUserName(vendType), getPassword(vendType));
                strToken = tokenResponse.getAccess_token();
                log.info("EKOEDC QUERY TOKEN:::::::: " + strToken);
                if (strToken.isEmpty() || strToken == null) {
                    electricityQueryResponse.setResponseDesc("UNABLE TO GET LOGIN TOKEN FROM EKOEDC.");
                    electricityQueryResponse.setResponseCode("06");
                    electricityQueryResponse.setErrorCode("06");
                    electricityQueryResponse.setUniqueTransId(reference);
                    return electricityQueryResponse;
                }

            } catch (Exception e) {
                e.printStackTrace();
                electricityQueryResponse.setResponseDesc("UNABLE TO GET LOGIN TOKEN FROM EKOEDC.");
                electricityQueryResponse.setResponseCode("06");
                electricityQueryResponse.setErrorCode("06");
                electricityQueryResponse.setUniqueTransId(reference);
                return electricityQueryResponse;

            }


            customerRequest.setIdVendor(getIdVendor(getUserName(vendType)));
            customerRequest.setCodUser(getCodeUser(getUserName(vendType)));
            customerRequest.setValue(electricityQueryRequest.getPayerId());
            customerRequest.setTotalPayment(amount);
            String[] resp = makeNestedCustomerInformationRequest(customerRequest, tokenResponse, vendType);

            if (resp.length > 0 && resp[0].equals("200")) {
                Customer[] customers = new Gson().fromJson(resp[1], Customer[].class);
                Customer customer = customers[0];
               // log.info("REDIS URL :::: " + redisUrl);

                if ((customer.getIndicatorPrePostAccount().equalsIgnoreCase("0") && vendType.equalsIgnoreCase("postpaid")) ||
                        (customer.getIndicatorPrePostAccount().equalsIgnoreCase("1") && vendType.equalsIgnoreCase("prepaid"))) {
                    log.info("********* YES ACCOUNT TYPE VALID**************    "+resp[1]);
                    log.info("********* YES ACCOUNT TYPE VALID**************    "+electricityQueryRequest.getPayerId());

                    redisService.setValue(electricityQueryRequest.getPayerId(), resp[1]);
                    log.info("REDIS PARAMETER :::: " + redisService.getValue(electricityQueryRequest.getPayerId()));
                    electricityQueryResponse.setAccountNumber(electricityQueryRequest.getPayerId());
                    electricityQueryResponse.setDisco("EKO");
                    electricityQueryResponse.setUniqueTransId(electricityQueryRequest.getReference());
                    electricityQueryResponse.setExternalReference(electricityQueryRequest.getReference());
                    electricityQueryResponse.setBusinessUnit(customer.getDistrictName() + " BUSINESS UNIT");
                    electricityQueryResponse.setCustomerAddress(customer.getServiceAddress());
                    electricityQueryResponse.setCustomerArrears(customer.getAccountBalance());
                    electricityQueryResponse.setTariffDesc(customer.getTariffDescription());
                    electricityQueryResponse.setCustomerName(customer.getName());
                    electricityQueryResponse.setRequestType(electricityQueryRequest.getType());
                    electricityQueryResponse.setCustomerType(electricityQueryRequest.getType());
                    electricityQueryResponse.setTariff(customer.getTariffDescription());
                    electricityQueryResponse.setTariffClass(customer.getTariffDescription());
                    electricityQueryResponse.setMinimumPurchase(customer.getIndicatorPrePostAccount().equals("0") ? "100" : "1000");

                    switch (customer.getCollectionInd()) {
                        case "0" -> {
                            electricityQueryResponse.setResponseCode("00");
                            electricityQueryResponse.setResponseDesc("Successful");
                            electricityQueryResponse.setErrorCode("0");
                        }
                        case "1" -> {
                            electricityQueryResponse.setResponseCode("06");
                            electricityQueryResponse.setResponseDesc("Inactive prepaid or postpaid meter");
                            electricityQueryResponse.setErrorCode("06");
                        }
                        case "2" -> {
                            electricityQueryResponse.setResponseCode("00");
                            electricityQueryResponse.setResponseDesc("Successful, This transaction can proceed, only debt is being collected, no tokens or advance");
                            electricityQueryResponse.setErrorCode("0");
                        }
                    }
                } else {
                    if (vendType.equalsIgnoreCase("postpaid")) {
                        electricityQueryResponse.setResponseDesc("Wrong Meter Type.....Use Prepaid");
                        electricityQueryResponse.setResponseCode("06");
                        electricityQueryResponse.setErrorCode("06");
                        electricityQueryResponse.setUniqueTransId(reference);
                        return electricityQueryResponse;
                    } else {
                        electricityQueryResponse.setResponseDesc("Wrong Meter Type...... Use Postpaid");
                        electricityQueryResponse.setResponseCode("06");
                        electricityQueryResponse.setErrorCode("06");
                        electricityQueryResponse.setUniqueTransId(reference);
                        return electricityQueryResponse;
                    }

                }

            } else {
                ErrorResponse errorResponse = new Gson().fromJson(resp[1], ErrorResponse.class);
                electricityQueryResponse.setResponseCode(errorResponse.getCode());
                electricityQueryResponse.setResponseDesc(buildErrorMessage(errorResponse));
                electricityQueryResponse.setErrorCode(errorResponse.getCode());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error verifying meter is::::::::::::: " + ex);
            electricityQueryResponse.setResponseCode("90");
            electricityQueryResponse.setResponseDesc("Unknown Meter/Account Number");
        }
        log.info("RESPONSE FROM QUERY:::::::::::::::::::: " + electricityQueryResponse);
        return electricityQueryResponse;
    }

    public String buildErrorMessage(ErrorResponse failResponse) {
        String message = "";
        if (failResponse.getMsgDeveloper().equals(failResponse.getMsgUser())) {
            message = failResponse.getMsgUser();
        } else {
            message = failResponse.getMsgUser() + " - " + failResponse.getMsgDeveloper();
        }
        return message;
    }

    private String[] makeNestedCustomerInformationRequest(CustomerRequest customerRequest, TokenResponse tokenResponse, String meterType) {
        Session session = SessionFactory.getSingleton();
        List<SearchCriterion> searchCriterionList = session.getSearchCriteria();
        int counter = 0;
        counter += 1;
        log.info("=== START :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY METER NUMBER === " + customerRequest.getValue());
        SearchCriterion searchCriterion = searchCriterionList.get(0); // Search by meter number
        customerRequest.setCodType(searchCriterion.getCodType());
        ///uncomment the line below
        ////String[] resp = getCustomerInformation(tokenResponse, customerRequest, meterType);

        ////////////comment the two lines below
        String[] resp = new String[2];
        resp[0] = "200";
        resp[1] = "[{\"account\":707601139,\"accountBalance\":88342.75,\"collectionInd\":2,\"districtCode\":20,\"districtName\":\"Ojo\",\"indicatorPrePostAccount\":0,\"meterSerial\":\" \",\"name\":\"ODEBUNMI TAJUDEEN  \",\"serviceAddress\":\"133 Imude Road Ajewum   Ojo Ojo\",\"tariffDescription\":\"Commercial 1PH - Band D\"}]";

        log.info("=== END :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY METER NUMBER === " + customerRequest.getValue());

        if (!resp[0].equals("200")) {
            counter += 1;
            log.info("=== START :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY ACCOUNT NUMBER === " + customerRequest.getValue());
            searchCriterion = searchCriterionList.get(1); // Search by account number
            customerRequest.setCodType(searchCriterion.getCodType());

            resp = getCustomerInformation(tokenResponse, customerRequest, meterType);
            log.info("=== END :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY ACCOUNT NUMBER === " + customerRequest.getValue());
            if (!resp[0].equals("200")) {
                counter += 1;
                log.info("=== START :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY CUSTOMER IDENTIFICATION === " + customerRequest.getValue());
                searchCriterion = searchCriterionList.get(2); // Search by customer identification number
                customerRequest.setCodType(searchCriterion.getCodType());
                resp = getCustomerInformation(tokenResponse, customerRequest, meterType);
                log.info("=== END :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY CUSTOMER IDENTIFICATION === " + customerRequest.getValue());
                if (!resp[0].equals("200")) {
                    counter += 1;
                    log.info("=== START :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY DRIVER LICENSE=== " + customerRequest.getValue());
                    searchCriterion = searchCriterionList.get(3); // Search by driver license
                    customerRequest.setCodType(searchCriterion.getCodType());
                    resp = getCustomerInformation(tokenResponse, customerRequest, meterType);
                    log.info("=== END :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY DRIVER LICENSE === " + customerRequest.getValue());
                    if (!resp[0].equals("200")) {
                        counter += 1;
                        log.info("=== START :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY OLD ACCOUNT === " + customerRequest.getValue());
                        searchCriterion = searchCriterionList.get(4); // Search by old account
                        customerRequest.setCodType(searchCriterion.getCodType());
                        resp = getCustomerInformation(tokenResponse, customerRequest, meterType);
                        log.info("=== END :: ATTEMPT NUMBER " + counter + " AT VERIFICATION : SEARCH BY OLD ACCOUNT === " + customerRequest.getValue());
                    }
                }
            }
        }

        return resp;
    }

    public String[] calculatePayment(TokenResponse tokenResponse, double amount, String meterSerial, String customerAccount, String vendType) throws Exception {
        log.info("****INSIDE calculatePayment *********");
        String[] resp = {"", ""};
        String idVendor = getIdVendor(vendType.equalsIgnoreCase("prepaid") ? prepaidUsername : postpaidUsername);
        String codUser = getCodeUser(vendType.equalsIgnoreCase("prepaid") ? prepaidUsername : postpaidUsername);

        String accessToken = tokenResponse.getAccess_token();

        PaymentValueRequest pvr = new PaymentValueRequest();
        pvr.setCodUser(codUser);
        pvr.setIdVendor(idVendor);
        pvr.setDebtPayment(0.00);
        pvr.setTotalPayment(amount);
        pvr.setMeterSerial(meterSerial);
        pvr.setAccount(customerAccount);
        String postRequest = new Gson().toJson(pvr);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + accessToken);

        String url = baseUrl + "venPayment/1.0.1/calculatePayment";
        resp = HTTPUtils.doAllREQUEST(2, 1, url, postRequest, header, 0, 1);

        log.info("EKO calculate Payment Value Response :: " + Arrays.toString(resp));

        return resp;
    }

    private String formatAmount(String amount) {
        Formatter formatter = new Formatter();
        formatter.format("%.2f", Double.parseDouble(amount));
        return formatter.toString();
    }

    private String[] getCustomerInformation(TokenResponse tokenResponse, CustomerRequest customerRequest, String meterType) {
        log.info("********* INSIDE getCustomerInformation*****************");
        String[] resp = {"", ""};
        String url = baseUrl + "venMeter/1.0.1/";

        String postRequest = new Gson().toJson(customerRequest);
        log.info("====== EKO QUERY REQUEST::::::" + postRequest);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + tokenResponse.getAccess_token());

        log.info("EKO Authorization header :: " + header);
        log.info("verification url " + url);
        resp = HTTPUtils.doAllREQUEST(2, 1, url, postRequest, header, 0, 1);

        log.info("RESPONSE FROM EKO " + meterType.toUpperCase() + " VENDING GATEWAY :: " + resp[1]);


        return resp;
    }

    public TokenResponse getAccessToken(String username, String password) throws Exception {
        Map<String, String> conProperties = new HashMap<>();
        log.info("getAccessToken Method() username :: " + username + " :: " + password);

        String tokenRequest = "grant_type=" + grantType + "&username=" + username + "&password=" + password;
        conProperties.put("Authorization", "Basic " + clientSecret);

        /////uncomment the line below
        //String[] resp;
        ////comment the line below
        String[] resp = new String[2];

        TokenResponse tokenResponse = new TokenResponse();

        try {
            ////uncoment the line below
            //resp = HTTPUtils.doAllREQUEST(2, 3, baseUrl + "token", tokenRequest, conProperties, 0, 1);
            //////////////////////comment the below two line
            resp[0] = "200";
            resp[1] = "{\"access_token\":\"86f3f71a-fb60-328e-9ace-372ff710901c\",\"scope\":\"default\",\"token_type\":\"Bearer\",\"expires_in\":\"7189\"}";
            if (resp.length > 0) {
                log.info("EKO Token Response Code :: " + resp[0]);
                log.info("EKO Token Response Data :: " + resp[1]);
                tokenResponse = new Gson().fromJson(resp[1], TokenResponse.class);
            }
            // log.info(tokenResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error fetching token is ::::::::: " + e);
        }
        return tokenResponse;
    }

    private String getIdVendor(String username) {
        String[] userParts = username.split("#");
        return userParts[0];
    }

    private String getCodeUser(String username) {
        String[] userParts = username.split("#");
        return userParts[1];
    }


//    public ElectricityProcessResponse process2(ElectricityProcessRequest electricityProcessRequest) {
//        log.info("*************INSIDE EKO PROCESS ******************");
//        log.info("****************EKO Process request::::::::::: " + electricityProcessRequest);
//        ElectricityProcessResponse electricityProcessResponse = new ElectricityProcessResponse();
//        String mobile = electricityProcessRequest.getMobile();
//        StringBuilder sb = new StringBuilder();
//        String output = "";
//        String vendType = "";
//        String vertical = "ELECTRICITY";
//        String tokenResponse = null;
//        String orderId = electricityProcessRequest.getReference();
//        Gson gson = new Gson();
//        VendResponseDto vendResponseDto = null;
//        VendFailed vendFailed = null;
//        String units = null;
//        String tariff = null;
//        String tax = null;
//        String debtRemaining = null;
//        String fault = null;
//        String freeUnits = null;
//        if (!mobile.isEmpty()) {
//
//            if (mobile.length() == 13 || mobile.length() == 11) {
//                if (mobile.length() == 13) {
//                    if (mobile.startsWith("234")) {
//                        mobile = "0" + mobile.substring(3, mobile.length());
//                    } else {
//                        mobile = "09087989094";
//                    }
//
//                } else {
//                    if (!mobile.startsWith("0")) {
//                        mobile = "09087989094";
//                    } else {
//                        if (mobile.startsWith("04")) {
//                            mobile = "09087989094";
//                        } else if (mobile.startsWith("02")) {
//                            mobile = "09087989094";
//                        } else if (mobile.startsWith("03")) {
//                            mobile = "09087989094";
//                        } else if (mobile.startsWith("05")) {
//                            mobile = "09087989094";
//                        } else if (mobile.startsWith("06")) {
//                            mobile = "09087989094";
//                        } else if (mobile.startsWith("00")) {
//                            mobile = "09087989094";
//                        }
//                    }
//                }
//            } else {
//                mobile = "09087989094";
//            }
//        } else {
//            mobile = "09087989094";
//        }
//        electricityProcessResponse.setRequestType("payment");
//        electricityProcessResponse.setDisco("EKO");
//        electricityProcessResponse.setAccountNumber(electricityProcessRequest.getPayerId());
//
//
//        if (electricityProcessRequest.getType().equalsIgnoreCase("1")) {
//            vendType = "PREPAID";
//        } else if (electricityProcessRequest.getType().equalsIgnoreCase("2")) {
//            vendType = "POSTPAID";
//        } else {
//
//            electricityProcessResponse.setResponseDesc("Invalid meter type");
//            electricityProcessResponse.setResponseCode("01");
//            electricityProcessResponse.setErrorCode("01");
//            electricityProcessResponse.setUniqueTransId(electricityProcessRequest.getReference());
//        }
//        try {
//
//            output = vendMeter(orderId, vendType, String.valueOf(electricityProcessRequest.getAmount()), mobile, electricityProcessRequest.getPayerId(), "EKO", vertical, buyPower_Payment_Type, buyPowerBaseUrl, buyPower_Public_Key);
//            String[] outcome = output.split("~");
//            String responseCode = outcome[0];
//
//            if (responseCode.equalsIgnoreCase("200")) {
//                log.info(":::::::::::::::::::::::::::::EKO payment notification was successfull*********");
//                vendResponseDto = gson.fromJson(outcome[1], VendResponseDto.class);
//
//                electricityProcessResponse.setAccountType(vendType);
//                electricityProcessResponse.setUniqueTransId(electricityProcessRequest.getReference());
//                electricityProcessResponse.setDisco("EKO");
//                if (vendType.equalsIgnoreCase("prepaid")) {
//                    tokenResponse = vendResponseDto.getData().getToken();
//                    units = vendResponseDto.getData().getUnits();
//                    tariff = vendResponseDto.getData().getTariff();
//                    tax = vendResponseDto.getData().getTax();
//                    debtRemaining = vendResponseDto.getData().getDebtRemaining();
//                    freeUnits = vendResponseDto.getData().getFreeUnits();
//                    MainTokenData maintoken = new MainTokenData();
//                    maintoken.setUnit(units);
//                    maintoken.setAmount(vendResponseDto.getData().getTotalAmountPaid());
//                    maintoken.setVat(tax);
//                    StringBuilder tokenSb = new StringBuilder();
//                    try {
//                        if (!vendResponseDto.getData().getParcels().isEmpty()) {
//                            log.info("*****************Parcel is available*******************");
//                            for (int i = 1; i < vendResponseDto.getData().getParcels().size(); i++) {
//                                tokenSb.append(vendResponseDto.getData().getParcels().get(i).getType()).append(": ").append(vendResponseDto.getData().getParcels().get(i).getContent()).append(",");
//                            }
//                        }
//                    } catch (Exception ex) {
//                        log.info("**** Error is:: " + ex);
//                    }
//                    maintoken.setToken(tokenResponse + "," + tokenSb);
//                    maintoken.setFixedCharge(" ");
//                    sb.append(electricityProcessRequest.getPayerId()).append(",").append(tokenResponse).append(",").append(units).append(",")
//                            .append(tariff).append(",").append(tax).append(",").append(debtRemaining);
//
//                    fault = sb.toString();
//                    electricityProcessResponse.setMainToken(maintoken);
//                    electricityProcessResponse.setExternalReference(vendResponseDto.getData().getVendRef());
//                    electricityProcessResponse.setResponseDesc("Successful");
//                    electricityProcessResponse.setUnitsPurchased(units);
//                    electricityProcessResponse.setResponseCode("00");
//                    electricityProcessResponse.setUnitsPurchased(vendResponseDto.getData().getUnits());
//                    electricityProcessResponse.setErrorCode("00");
//                    electricityProcessResponse.setAccountNumber(electricityProcessRequest.getPayerId());
//                    electricityProcessResponse.setFault(fault);
//                    electricityProcessResponse.setCustomerArrears(debtRemaining);
//                    electricityProcessResponse.setTax(tax);
//                    electricityProcessResponse.setTariff(tariff);
//
//                } else {
//                    tokenResponse = vendResponseDto.getData().getToken();
//
//                    units = null;
//                    tariff = vendResponseDto.getData().getTariff();
//                    tax = vendResponseDto.getData().getTax();
//                    debtRemaining = vendResponseDto.getData().getDebtRemaining();
//                    freeUnits = vendResponseDto.getData().getFreeUnits();
//                    MainTokenData maintoken = new MainTokenData();
//                    maintoken.setUnit(units);
//                    maintoken.setAmount(vendResponseDto.getData().getVendAmount());
//                    maintoken.setVat(tax);
//                    maintoken.setFixedCharge(" ");
//                    electricityProcessResponse.setMainToken(maintoken);
//                    electricityProcessResponse.setExternalReference(vendResponseDto.getData().getVendRef());
//                    electricityProcessResponse.setResponseCode("00");
//                    electricityProcessResponse.setTax(tax);
//                    electricityProcessResponse.setTariff(tariff);
//                    electricityProcessResponse.setErrorCode("00");
//                    electricityProcessResponse.setResponseDesc("Successful");
//                    electricityProcessResponse.setBusinessUnit("");
//                    electricityProcessResponse.setAccountNumber(electricityProcessRequest.getPayerId());
//                    electricityProcessResponse.setFault(fault);
//                    electricityProcessResponse.setCustomerArrears(debtRemaining);
//                }
//            } else {
//                vendFailed = gson.fromJson(outcome[1], VendFailed.class);
//                log.info(":::::::::::::::::::::::::::EKO payment notification was NOT successful");
//                String message = "";
//                if (vendFailed.getMessage() == null || "".equals(vendFailed.getMessage())) {
//                    message = "Transaction Failed";
//                } else {
//                    message = vendFailed.getMessage();
//                }
//                electricityProcessResponse.setResponseDesc(message);
//                electricityProcessResponse.setResponseCode("06");
//                electricityProcessResponse.setErrorCode(String.valueOf(vendFailed.getResponseCode()));
//                electricityProcessResponse.setUniqueTransId(electricityProcessRequest.getReference());
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("::::::::::::::the Exception Posting to EKO :::::::" + e.getMessage());
//            electricityProcessResponse.setResponseDesc("Error occurred; Exception");
//            electricityProcessResponse.setResponseCode("08");
//            electricityProcessResponse.setErrorCode("08");
//            electricityProcessResponse.setUniqueTransId(electricityProcessRequest.getReference());
//        }
//
//
//        log.info("=====================the reponse from eko process: " + gson.toJson(electricityProcessResponse));
//        return electricityProcessResponse;
//    }


    public String vendMeter(String orderId, String vendType, String amount, String phone, String meterNo, String disco,
                            String vertical, String paymentType, String baseUrl, String publicKey) throws SocketTimeoutException, IOException, Exception {
        log.info("............................INSIDE EKO vendMeter for notification................................. ");
        HttpURLConnection con = null;
        String inputLine;
        String output = "";
        String finalUrl = null;
        Gson gson = new Gson();
        String USER_AGENT = "Mozilla/5.0";
        try {
            finalUrl = baseUrl + "vend?strict=1";
            log.info("................ EKO vendMeter for notification url :::: " + finalUrl);
            VendRequestDto vendRequestDto = new VendRequestDto(orderId, vendType, amount, phone, meterNo, disco, vertical, paymentType);

            String jsonStringParameters = gson.toJson(vendRequestDto);
            log.info("................ EKO vendMeter for notification request :::: " + jsonStringParameters);
            URL url = new URL(finalUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setConnectTimeout(10000);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + publicKey);
            con.setRequestProperty("Content-Type", "application/json");

            OutputStream os = con.getOutputStream();
            os.write(jsonStringParameters.getBytes());
            os.flush();
            os.close();
            int responseCode = con.getResponseCode();
            log.info("::::::::::::::::::::::::Response Code for EKO payment notification ::: " + responseCode);
            BufferedReader in;
            StringBuffer response = new StringBuffer();
            if (responseCode == 200) {
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
            log.info("::::::::::::::::::::::Response from BUYPOWER for EKO payment notification : >>>> " + output);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                log.info(":::::::::::::::::::closing the conn");
                con.disconnect();
            }
        }
        return output;
    }

    @Override
    public ElectricityProcessResponse process(ElectricityProcessRequest electricityProcessRequest) {
        log.info("*************INSIDE PROCESS SERVICE ******************");
        log.info("****************Process request:::::::::::::" + electricityProcessRequest);


        ElectricityProcessResponse electricityProcessResponse = new ElectricityProcessResponse();
        if (electricityProcessRequest.getAmount() <= 0) {
            electricityProcessResponse.setResponseCode("06");
            electricityProcessResponse.setResponseDesc("Invalid Amount");
            electricityProcessResponse.setErrorCode("06");
            electricityProcessResponse.setUniqueTransId(electricityProcessRequest.getReference());
            return electricityProcessResponse;
        } else if (electricityProcessRequest.getPayerId().isEmpty() || electricityProcessRequest.getPayerId() == null) {
            electricityProcessResponse.setResponseCode("06");
            electricityProcessResponse.setResponseDesc("Invalid Account");
            electricityProcessResponse.setErrorCode("06");
            electricityProcessResponse.setUniqueTransId(electricityProcessRequest.getReference());
            return electricityProcessResponse;
        } else if (electricityProcessRequest.getReference().isEmpty() || electricityProcessRequest.getReference() == null) {
            electricityProcessResponse.setResponseCode("06");
            electricityProcessResponse.setResponseDesc("Invalid Reference");
            electricityProcessResponse.setErrorCode("06");
            return electricityProcessResponse;
        } else if (electricityProcessRequest.getType().isEmpty() || electricityProcessRequest.getType() == null) {
            electricityProcessResponse.setResponseCode("06");
            electricityProcessResponse.setResponseDesc("Invalid Action Type");
            electricityProcessResponse.setErrorCode("06");
            electricityProcessResponse.setUniqueTransId(electricityProcessRequest.getReference());
            return electricityProcessResponse;
        } else if (electricityProcessRequest.getMobile().isEmpty() || electricityProcessRequest.getMobile() == null) {
            electricityProcessResponse.setResponseCode("06");
            electricityProcessResponse.setResponseDesc("Invalid Mobile");
            electricityProcessResponse.setErrorCode("06");
            electricityProcessResponse.setUniqueTransId(electricityProcessRequest.getReference());
            return electricityProcessResponse;
        } else if (electricityProcessRequest.getPaymentChannel().isEmpty() || electricityProcessRequest.getPaymentChannel() == null) {
            electricityProcessResponse.setResponseCode("06");
            electricityProcessResponse.setResponseDesc("Invalid Payment Channel");
            electricityProcessResponse.setErrorCode("06");
            electricityProcessResponse.setUniqueTransId(electricityProcessRequest.getReference());
            return electricityProcessResponse;
        }
        String vendType = electricityProcessRequest.getType();
        String reference = electricityProcessRequest.getReference();

        if (vendType.equalsIgnoreCase("1")) {
            vendType = "prepaid";
        } else if (vendType.equalsIgnoreCase("2")) {
            vendType = "postpaid";
        } else {
            electricityProcessResponse.setResponseCode("06");
            electricityProcessResponse.setResponseDesc("Invalid meter type");
            electricityProcessResponse.setErrorCode("06");
            electricityProcessResponse.setUniqueTransId(electricityProcessRequest.getReference());
            return electricityProcessResponse;
        }
        double amount = electricityProcessRequest.getAmount();
        if (vendType.equalsIgnoreCase("prepaid") && amount < 1000) {
            electricityProcessResponse.setResponseCode("06");
            electricityProcessResponse.setResponseDesc("Amount cannot be less than 1000");
            electricityProcessResponse.setErrorCode("06");
            electricityProcessResponse.setUniqueTransId(electricityProcessRequest.getReference());
            return electricityProcessResponse;
        }

        if (vendType.equalsIgnoreCase("postpaid") && amount < 100) {
            electricityProcessResponse.setResponseCode("06");
            electricityProcessResponse.setResponseDesc("Amount cannot be less than 100");
            electricityProcessResponse.setErrorCode("06");
            electricityProcessResponse.setUniqueTransId(electricityProcessRequest.getReference());
            return electricityProcessResponse;
        }


        Customer customer;
        Customer[] customers;
        PaymentResponse paymentResponse;
        String[] makePaymentResp = {"", ""};
        try {

            try {
                String strCustomers;
                strCustomers =  redisService.getValue(electricityProcessRequest.getPayerId());
                log.info("strCustomers = " + strCustomers);
                customers = new Gson().fromJson(strCustomers, Customer[].class);

                customer = customers[0];
            } catch (Exception ex) {
                log.error("Error processing is :::::: " + ex);
                electricityProcessResponse.setResponseCode("90");
                electricityProcessResponse.setResponseDesc("Unknown Meter/Account Number, please do query first.");
                electricityProcessResponse.setErrorCode("90");
                electricityProcessResponse.setUniqueTransId(electricityProcessRequest.getReference());
                return electricityProcessResponse;
            }

            String strToken;
            TokenResponse tokenResponse = new TokenResponse();
            try {
                tokenResponse = getAccessToken(getUserName(vendType), getPassword(vendType));
                strToken = tokenResponse.getAccess_token();
                log.info("EKOEDC PROCESS TOKEN:::::::: " + strToken);
                if (strToken.isEmpty() || strToken == null) {
                    electricityProcessResponse.setResponseDesc("UNABLE TO GET LOGIN TOKEN FROM EKOEDC.");
                    electricityProcessResponse.setResponseCode("06");
                    electricityProcessResponse.setErrorCode("06");
                    electricityProcessResponse.setUniqueTransId(reference);
                    return electricityProcessResponse;
                }

            } catch (Exception e) {
                e.printStackTrace();
                electricityProcessResponse.setResponseDesc("UNABLE TO GET LOGIN TOKEN FROM EKOEDC.");
                electricityProcessResponse.setResponseCode("06");
                electricityProcessResponse.setErrorCode("06");
                electricityProcessResponse.setUniqueTransId(reference);
                return electricityProcessResponse;

            }


            String[] calcPaymentResp;
            calcPaymentResp = calculatePayment(tokenResponse, amount, customer.getMeterSerial(), customer.getAccount(), vendType);

            if (calcPaymentResp[0].equals("200")) {

                makePaymentResp = makePayment(tokenResponse.getAccess_token(), customer.getMeterSerial(), amount,
                        reference, electricityProcessRequest.getPaymentChannel(), vendType, customer.getAccount(), electricityProcessRequest.getPayerId());

            } else if (calcPaymentResp[0].equals("204") && vendType.equalsIgnoreCase("postpaid")) {

                makePaymentResp = makePayment(tokenResponse.getAccess_token(), customer.getMeterSerial(), amount,
                        reference, electricityProcessRequest.getPaymentChannel(), vendType, customer.getAccount(), electricityProcessRequest.getPayerId());

            } else {
                ErrorResponse errorResponse = new Gson().fromJson(calcPaymentResp[1], ErrorResponse.class);
                electricityProcessResponse.setResponseCode(errorResponse.getCode());
                electricityProcessResponse.setResponseDesc(buildErrorMessage(errorResponse));
                electricityProcessResponse.setErrorCode("06");
                electricityProcessResponse.setUniqueTransId(reference);
                return electricityProcessResponse;
            }

            if (!makePaymentResp[0].equals("201")) {
                if (makePaymentResp[0].equals("500")) {
                    log.info("=================== CALLING EKO AGAIN AFTER TIMEOUT==================================");

                    try {
                        // Sleep for 30 seconds (30,000 milliseconds)
                        Thread.sleep(36000);
                    } catch (InterruptedException e) {
                        // Handle the exception if the thread is interrupted
                        Thread.currentThread().interrupt(); // Preserve interrupt status
                        System.out.println("Thread was interrupted");
                    }
                    log.info("=================== CALLING EKO REQUERY AFTER WAITING FOR 40000ms==================================");
                    makePaymentResp = makePayment(tokenResponse.getAccess_token(), customer.getMeterSerial(), amount,
                            reference, electricityProcessRequest.getPaymentChannel(), vendType, customer.getAccount(), electricityProcessRequest.getPayerId());

                    if (!makePaymentResp[0].equals("201")) {
                        ErrorResponse errorResponse = new Gson().fromJson(calcPaymentResp[1], ErrorResponse.class);
                        electricityProcessResponse.setResponseCode(errorResponse.getCode());
                        electricityProcessResponse.setResponseDesc(buildErrorMessage(errorResponse));
                        electricityProcessResponse.setErrorCode("06");
                        electricityProcessResponse.setUniqueTransId(reference);
                        return electricityProcessResponse;
                    }
                    paymentResponse = new Gson().fromJson((makePaymentResp[1]), PaymentResponse.class);
                    electricityProcessResponse = computeSuccessProcessResponse(paymentResponse, customer);
                    electricityProcessResponse.setUniqueTransId(reference);
                    electricityProcessResponse.setRequestType(electricityProcessRequest.getType());
                    electricityProcessResponse.setAccountType(electricityProcessRequest.getType());
                    electricityProcessResponse.setNewAccountNumber(electricityProcessRequest.getPayerId().equals(paymentResponse.getAccount()) ? electricityProcessRequest.getPayerId() : paymentResponse.getAccount());


                } else {
                    ErrorResponse errorResponse = new Gson().fromJson(calcPaymentResp[1], ErrorResponse.class);
                    electricityProcessResponse.setResponseCode(errorResponse.getCode());
                    electricityProcessResponse.setResponseDesc(buildErrorMessage(errorResponse));
                    electricityProcessResponse.setErrorCode("06");
                    electricityProcessResponse.setUniqueTransId(reference);
                    return electricityProcessResponse;
                }
            } else {
                paymentResponse = new Gson().fromJson((makePaymentResp[1]), PaymentResponse.class);
                electricityProcessResponse = computeSuccessProcessResponse(paymentResponse, customer);
                electricityProcessResponse.setUniqueTransId(reference);
                electricityProcessResponse.setRequestType(electricityProcessRequest.getType());
                electricityProcessResponse.setAccountType(electricityProcessRequest.getType());
                electricityProcessResponse.setNewAccountNumber(electricityProcessRequest.getPayerId().equals(paymentResponse.getAccount()) ? electricityProcessRequest.getPayerId() : paymentResponse.getAccount());

            }
            Gson gson = new Gson();
            customer.setAccountBalance(String.valueOf(paymentResponse.getAccountBalance()));
            customer = new Gson().fromJson(new Gson().toJson(customer), Customer.class);

            customers[0] = customer;
            log.info("What we are about to save  on redis " + gson.toJson(customers));
            redisService.setValue(electricityProcessRequest.getPayerId(), gson.toJson(customers));


        } catch (Exception ex) {
            log.error("Error processing is :::::: " + ex);
            electricityProcessResponse.setResponseCode("91");
            electricityProcessResponse.setResponseDesc("Error occurred, please try again.");
        }
        return electricityProcessResponse;
    }

    private ElectricityProcessResponse computeSuccessProcessResponse(PaymentResponse paymentResponse, Customer customer) {
        ElectricityProcessResponse electricityProcessResponse = new ElectricityProcessResponse();
        electricityProcessResponse.setResponseCode("00");
        electricityProcessResponse.setResponseDesc("Successful");
        electricityProcessResponse.setDisco("EKO");
        electricityProcessResponse.setAccountNumber(paymentResponse.getAccount());
        electricityProcessResponse.setCustomerName(paymentResponse.getCustomerName());
        electricityProcessResponse.setCustomerAddress(customer.getServiceAddress());
        electricityProcessResponse.setVendor(String.valueOf(paymentResponse.getIdVendor()));
        electricityProcessResponse.setCustomerArrears(String.valueOf(paymentResponse.getAccountBalance()));
        electricityProcessResponse.setAmount(String.valueOf(paymentResponse.getTotalPayment()));
        electricityProcessResponse.setTariffDescription(paymentResponse.getTariffDescription());
        electricityProcessResponse.setBalance(String.valueOf(paymentResponse.getDebtPayment()));
        electricityProcessResponse.setUnitsPayment(String.valueOf(paymentResponse.getUnitsPayment()));
        electricityProcessResponse.setExternalReference(paymentResponse.getReceipt());
        MainTokenData mainTokenData = new MainTokenData();
        mainTokenData.setAmount(String.valueOf(paymentResponse.getTotalPayment()));
        mainTokenData.setUnit(String.valueOf(paymentResponse.getUnits()));
        mainTokenData.setVat(retrieveVAT(paymentResponse));
        mainTokenData.setToken(fetchTokens(paymentResponse.getListtoken()));
        mainTokenData.setKeyDataSGC(String.valueOf(paymentResponse.getKeyDataSGC()));
        mainTokenData.setKeyDataTI(String.valueOf(paymentResponse.getKeyDataTI()));
        mainTokenData.setKeyDataKRN(String.valueOf(paymentResponse.getKeyDataKRN()));
        mainTokenData.setMapUnits(String.valueOf(paymentResponse.getMapUnits()));
        mainTokenData.setMapAmount(String.valueOf(paymentResponse.getMapAmount()));
        mainTokenData.setMapTokens(fetchTokens(paymentResponse.getMapTokens()));
        mainTokenData.setKctTokens(fetchTokens(paymentResponse.getKctTokens()));
        electricityProcessResponse.setMainToken(mainTokenData);
        electricityProcessResponse.setBusinessUnit(paymentResponse.getDistrictName() + " EKO BUSINESS UNIT");
        electricityProcessResponse.setErrorCode("00");


        if (!paymentResponse.getUnitsTopUp().isEmpty()) {
            try {

                electricityProcessResponse.setUnitsPurchased(String.valueOf(paymentResponse.getUnitsTopUp().get(1).getUnits()));
            } catch (Exception ex) {
                try {
                    electricityProcessResponse.setUnitsPurchased(String.valueOf(paymentResponse.getUnitsTopUp().get(0).getAmount()));
                } catch (Exception e) {
                    electricityProcessResponse.setUnitsPurchased(String.valueOf(paymentResponse.getUnitsPayment()));
                }
            }

        } else {

            electricityProcessResponse.setUnitsPurchased(String.valueOf(paymentResponse.getUnitsPayment()));
        }


        return electricityProcessResponse;
    }

    public String retrieveVAT(PaymentResponse paymentDetailsResponse) {
        String vat = "";

        if (!paymentDetailsResponse.getUnitsTopUp().isEmpty()) {
            for (UnitTopUp unitTopUp : paymentDetailsResponse.getUnitsTopUp()) {
                if (unitTopUp.getConceptName().equalsIgnoreCase("vat")) {
                    vat = unitTopUp.getAmount() + "";
                    break;
                }
            }
        }

        return vat;
    }

    private String fetchTokens(List<String> tokens) {
        int count = 0;
        String firstToken = "";

        for (String token : tokens) {
            ++count;
            firstToken += token;
            firstToken += tokens.size() > count ? "," : "";
        }
        return firstToken;
    }

    private String[] makePayment(String accessToken, String meterSerial, double amount, String uniqueReference,
                                 String paymentChannel, String vendType, String account, String payerId) throws Exception {
        log.info("Make EKO Payment() for Customer with meter  :: " + payerId);
        PaymentRequest paymentRequest = new PaymentRequest();
        int channel;
        String channelKey = uniqueReference.substring(0, 2);
        if (!paymentChannel.startsWith("0") && channelKey.equals("09")) {
            channel = Integer.parseInt(paymentChannel);
        } else {
            if (channelKey.equals("01") || channelKey.equals("09")) {
                channel = 15;
            } else if (channelKey.equals("02")) {
                if (uniqueReference.contains("02USD")) {
                    channel = 12;
                } else if (uniqueReference.contains("02POS")) {
                    channel = 1;
                } else {
                    channel = 7;
                }
            } else if (channelKey.equals("03")) {
                channel = 1;
            } else if (channelKey.equals("11")) {
                channel = 7;
            } else if (channelKey.equals("05")) {
                channel = 5;
            } else {
                channel = 5;
            }
        }

        paymentRequest.setIdVendor(getIdVendor(vendType.equalsIgnoreCase("prepaid") ? prepaidUsername : postpaidUsername));
        paymentRequest.setCodUser(getCodeUser(vendType.equalsIgnoreCase("prepaid") ? prepaidUsername : postpaidUsername));
        paymentRequest.setMeterSerial(meterSerial);
        paymentRequest.setTotalPayment(amount);
        paymentRequest.setRequestID(uniqueReference);
        paymentRequest.setChannel(channel);
        paymentRequest.setAccount(account);

        String postRequest = new Gson().toJson(paymentRequest);
        log.info("Make Payment Request FOR EKO :: postRequest :: " + postRequest);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + accessToken);

        String url = baseUrl + "venPayment/1.0.1/makePayment";

        log.info("EKO PAYMENT URL :: " + url);
        log.info("EKO PAYMENT header :: " + header);

        String[] resp = HTTPUtils.doAllREQUEST(2, 1, url, postRequest, header, 0, 1);

        log.info("Payment Response FROM EKO :: " + Arrays.toString(resp));

        return resp;
    }


    @Override
    public ElectricityProcessResponse reQuery(ElectricityReQueryRequest electricityReProcessRequest) {
        ElectricityProcessResponse electricityProcessResponse = new ElectricityProcessResponse();

        if (electricityReProcessRequest.getUniqueTransId().isEmpty() || electricityReProcessRequest.getUniqueTransId() == null) {
            electricityProcessResponse.setResponseCode("06");
            electricityProcessResponse.setResponseDesc("UniqueTransId cannot be empty");
            return electricityProcessResponse;
        } else if (electricityReProcessRequest.getType().isEmpty() || electricityReProcessRequest == null) {
            electricityProcessResponse.setResponseCode("06");
            electricityProcessResponse.setResponseDesc("Invalid Action Type");
            return electricityProcessResponse;
        }


        String vendType = electricityReProcessRequest.getType();
        String reference = electricityReProcessRequest.getUniqueTransId();
        Customer customer = null;
        if (vendType.equalsIgnoreCase("1")) {
            vendType = "prepaid";
        } else if (vendType.equalsIgnoreCase("2")) {
            vendType = "postpaid";
        } else {
            electricityProcessResponse.setResponseCode("06");
            electricityProcessResponse.setResponseDesc("Invalid meter type");
            electricityProcessResponse.setErrorCode("06");
            electricityProcessResponse.setUniqueTransId(electricityReProcessRequest.getReference());
            return electricityProcessResponse;
        }

        PaymentResponse paymentResponse;
        PayInfoRequest payInfoRequest = new PayInfoRequest();
        payInfoRequest.setIdVendor(getIdVendor(vendType.equalsIgnoreCase("prepaid") ? prepaidUsername : postpaidUsername));
        payInfoRequest.setCodUser(getCodeUser(vendType.equalsIgnoreCase("prepaid") ? prepaidUsername : postpaidUsername));
        payInfoRequest.setRequestID(electricityReProcessRequest.getUniqueTransId());


        String strToken;
        TokenResponse tokenResponse = new TokenResponse();
        try {
            tokenResponse = getAccessToken(getUserName(vendType), getPassword(vendType));
            strToken = tokenResponse.getAccess_token();
            log.info("EKOEDC RE-QUERY TOKEN:::::::: " + strToken);
            if (strToken.isEmpty() || strToken == null) {
                electricityProcessResponse.setResponseDesc("UNABLE TO GET LOGIN TOKEN FROM EKOEDC.");
                electricityProcessResponse.setResponseCode("06");
                electricityProcessResponse.setErrorCode("06");
                electricityProcessResponse.setUniqueTransId(reference);
                return electricityProcessResponse;
            }

        } catch (Exception e) {
            e.printStackTrace();
            electricityProcessResponse.setResponseDesc("UNABLE TO GET LOGIN TOKEN FROM EKOEDC.");
            electricityProcessResponse.setResponseCode("06");
            electricityProcessResponse.setErrorCode("06");
            electricityProcessResponse.setUniqueTransId(reference);
            return electricityProcessResponse;

        }


        String url = baseUrl + "/venPayment/1.0.1/paymentInfo";
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + tokenResponse.getAccess_token());
        String postRequest = new Gson().toJson(payInfoRequest);
        String[] resp = HTTPUtils.doAllREQUEST(2, 1, url, postRequest, header, 0, 1);

        if (resp.length > 0 && resp[0].equals("200")) {
            paymentResponse = new Gson().fromJson((resp[1]), PaymentResponse.class);
            customer.setServiceAddress(" ");
            electricityProcessResponse = computeSuccessProcessResponse(paymentResponse, customer);
            electricityProcessResponse.setUniqueTransId(electricityReProcessRequest.getReference());
        } else {
            ErrorResponse errorResponse = new Gson().fromJson(resp[1], ErrorResponse.class);
            electricityProcessResponse.setResponseCode(errorResponse.getCode());
            electricityProcessResponse.setResponseDesc(buildErrorMessage(errorResponse));
        }

        return electricityProcessResponse;
    }

    @Override
    public PingResponse ping(ElectricityQueryRequest electricityQueryRequest) {
        //todo implement ping
        String reference = electricityQueryRequest.getReference();
        PingResponse pingResponse = new PingResponse();
        String accountNumber = pingAccountNumber;
        String pinphoneNumber = pingMobile;
        double amount = 2000;

        ElectricityQueryRequest pingElectricityQueryRequest = new ElectricityQueryRequest();
        pingElectricityQueryRequest.setType("1");
        pingElectricityQueryRequest.setPayerId(accountNumber);
        pingElectricityQueryRequest.setReference(reference);
        pingElectricityQueryRequest.setAmount(amount);
        pingElectricityQueryRequest.setChannel("01");
        pingElectricityQueryRequest.setMobile(pingMobile);

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> the ping mobile number is: " + pingMobile);

        ElectricityQueryResponse electricityQueryResponse = query(pingElectricityQueryRequest);

        if (electricityQueryResponse.getResponseCode().equalsIgnoreCase("00")) {
            pingResponse.setCode("00");
            pingResponse.setMessage("success");
        } else {
            pingResponse.setMessage("Failed");
            pingResponse.setCode("00");
        }

        return pingResponse;
    }


}
