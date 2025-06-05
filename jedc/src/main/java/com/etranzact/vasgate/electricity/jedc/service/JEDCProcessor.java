package com.etranzact.vasgate.electricity.jedc.service;


import com.etranzact.vasgate.electricity.jedc.dto.PaymentResponse;
import com.etranzact.vasgate.electricity.jedc.dto.QueryResponse;
import com.etranzact.vasgate.electricity.jedc.dto.VerifyPayment;

import com.etranzact.vasgate.electricity.phcnnode.PHCNNode;

import com.etranzact.vasgate.electricity.phcnnode.dto.*;
import com.etranzact.vasgate.electricity.jedc.utils.AccessCodeSingleton;
import com.etranzact.vasgate.electricity.jedc.actionmenu.EnumResponseMsg;
import com.etranzact.vasgate.electricity.jedc.utils.JosService;
import com.etranzact.vasgate.electricity.redisutility.redisservice.NewVasgateRedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
//import org.apache.log4j.Logger;
import java.net.SocketTimeoutException;

import java.util.HashMap;

@Slf4j
@Service
@Component("phcnjos")
public class JEDCProcessor extends PHCNNode {


    @Value("${JOS_WS_TOKEN}")
    private String accessToken;
    
    @Value("${JOS_WS_PRIVATE_KEY}")
    private String PRIVATE_KEY;

    private static long lastTokenTime = 0L;

    @Value("${POSTPAID_JOS_WS_BASEURL}")
    private String postpaidUrl;
    @Value("${PREPAID_JOS_WS_BASEURL}")
    private String prepaidUrl;
    @Value("${JOS_VERIFY_ENDPOINT}")
    private String verifyEndpoint;
    @Value("${JOS_MAKEPAYMENT_ENDPOINT}")
    private String paymentEnpoint;
    @Value("${JOS_WALLETBALLANCE_ENDPOINT}")
    private String walletbalance;
    @Value("${JOS_VERIFYPAYMENT_ENDPOINT}")
    private String verifypayment;
    @Value("${JOS_TRANSACTIONHISTORY_ENDPOINT}")
    private String transhistory;

    @Value("${JOS_PING_METERNO:44000316354}")
    private String JOS_PING_METERNO;

    @Autowired
    private NewVasgateRedisService redisService;
    private static HashMap<String, String> paymentChannelMap = null;

//    public JEDCProcessor(Logger inLogger) {
//        logger = inLogger;
//    }

//    public JEDCProcessor() {
//    }
//
     JosService jos = new JosService();
    static String token = "";
    static AccessCodeSingleton accessCodeSingleton = null;

//    static {
//        try {
//
//            properties = new Properties();
//            // jos = new JosService(logger);
//            properties.load(new FileInputStream(new File("cfg/phcndb-config.properties")));
//
//            accessCodeSingleton = AccessCodeSingleton.getInstance();
//
//            if (InitProcessor.getInitParameters() == null) {
//                HashMap<String, String> initParameters = new HashMap<>();
//                Enumeration<String> params = paramNames();
//
//                while (params.hasMoreElements()) {
//                    String key = params.nextElement();
//                    initParameters.put(key, param(key));
//                }
//
//                InitProcessor.setInitParameters(initParameters);
//            }
//
//            postpaidUrl = InitProcessor.getProp("POSTPAID_JOS_WS_BASEURL");
//            prepaidUrl = InitProcessor.getProp("PREPAID_JOS_WS_BASEURL");
//            verifyEndpoint = InitProcessor.getProp("JOS_VERIFY_ENDPOINT");
//            paymentEnpoint = InitProcessor.getProp("JOS_MAKEPAYMENT_ENDPOINT");
//            walletbalance = InitProcessor.getProp("JOS_WALLETBALLANCE_ENDPOINT");
//            verifypayment = InitProcessor.getProp("JOS_VERIFYPAYMENT_ENDPOINT");
//            transhistory = InitProcessor.getProp("JOS_TRANSACTIONHISTORY_ENDPOINT");
//            PRIVATE_KEY = InitProcessor.getProp("JOS_WS_PRIVATE_KEY");
//            accessToken = InitProcessor.getProp("JOS_WS_TOKEN");
//
//        } catch (Exception e) {
//            System.out.println("Failed loading initial parameter " + e);
//            e.printStackTrace();
//        }
//    }

//    public static String param(String s) {
//        String val = null;
//        try {
//            val = (String) properties.get(s);
//        } catch (Exception e) {
//            System.out.println("error getting parameter " + s + " " + e.getMessage());
//        }
//
//        return val;
//    }
//
//    public static Enumeration paramNames() {
//        return properties.propertyNames();
//    }

    @Override
    public ElectricityQueryResponse query(ElectricityQueryRequest electricityQueryRequest) {

        ObjectMapper objectMapper = new ObjectMapper();
        ElectricityQueryResponse response = new ElectricityQueryResponse();
        try {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>the request sent for Jos electricity: "+objectMapper.writeValueAsString(electricityQueryRequest));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if(electricityQueryRequest.getType().equalsIgnoreCase("2")){
            response =  doPostPaidCustomerInfo(electricityQueryRequest);
        }else if(electricityQueryRequest.getType().equalsIgnoreCase("1")){
            response =  doPrepaidInfoPosting(electricityQueryRequest);
        }
        return response;
    }

    @Override
    public ElectricityProcessResponse process(ElectricityProcessRequest electricityProcessRequest) {

        ElectricityProcessResponse response = new ElectricityProcessResponse();
        if(electricityProcessRequest.getType().equalsIgnoreCase("1")){
            response = doPrepaidTransactionPosting(electricityProcessRequest);
        }else if(electricityProcessRequest.getType().equalsIgnoreCase("2")){
            response = doPostPaidTransactionPosting(electricityProcessRequest);
        }
        return response;
    }

    @Override
    public PingResponse ping(ElectricityQueryRequest electricityQueryRequest) {

        Gson gson = new Gson();
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> the ping request sent to JEDC is: "+gson.toJson(electricityQueryRequest));
        ElectricityQueryRequest pinQueryRequest = new ElectricityQueryRequest();
        pinQueryRequest.setPayerId(JOS_PING_METERNO);
        pinQueryRequest.setType("prepaid");

        ElectricityQueryResponse electricityQueryResponse = doPrepaidInfoPosting(pinQueryRequest);

        PingResponse pingResponse = new PingResponse();

        if(electricityQueryResponse.getResponseCode().equalsIgnoreCase("00")){
            pingResponse.setMessage("Succcess");
            pingResponse.setCode("00");
            return pingResponse;
        }else{
            pingResponse.setMessage("failed");
            pingResponse.setCode("01");
            return pingResponse;
        }

    }

    @Override
    public ElectricityProcessResponse reQuery(ElectricityReQueryRequest electricityReProcessRequest) {

        ElectricityProcessResponse electricityProcessResponse = new ElectricityProcessResponse();
        electricityProcessResponse.setUnitsPurchased(electricityReProcessRequest.getReference());
        electricityProcessResponse.setResponseDesc(EnumResponseMsg.REQUERY_DOES_NOT_EXIST.responseMsg);
        electricityProcessResponse.setErrorCode(EnumResponseMsg.REQUERY_DOES_NOT_EXIST.responseCode);
        electricityProcessResponse.setResponseCode(EnumResponseMsg.REQUERY_DOES_NOT_EXIST.responseCode);

        return electricityProcessResponse;

    }

    public ElectricityQueryResponse doPostPaidCustomerInfo(ElectricityQueryRequest electricityQueryRequest){
        log.info(":::::::::::::::::::::::;POSTPAID CUSTOMER INFO REQUEST:::::" + electricityQueryRequest);

        String result = "";
        String faultparam = null;
        String accessCode = null;
        String feeder = null;
        String tariffRate = null;
        String outStanding = null;
        String customername = null;

        JsonObject verificationDataObj = new JsonObject();
        ElectricityQueryResponse response = new ElectricityQueryResponse();

        try{
            if (token != null){
                String accountOrMeterNo = electricityQueryRequest.getPayerId();

                verificationDataObj.addProperty("requestType", "verification");
                verificationDataObj.addProperty("disco", "JOS");
                verificationDataObj.addProperty("accountType", "postpaid");
                verificationDataObj.addProperty("accountNumber", accountOrMeterNo);

                response.setRequestType("verification");
                response.setDisco("JOS");
                response.setAccountNumber(accountOrMeterNo);

                QueryResponse electricityQueryResponse = jos.customerQuery(accountOrMeterNo, "postpaid", postpaidUrl, verifyEndpoint, accessToken, PRIVATE_KEY);
                String businessUnit = null;
                if (electricityQueryResponse.getStatus().equals("100")) {

                    verificationDataObj.addProperty("customerName", electricityQueryResponse.getCustomer().getName());
                    verificationDataObj.addProperty("customerAddress", electricityQueryResponse.getCustomer().getAddress());

                    verificationDataObj.addProperty("businessUnit", EnumResponseMsg. business_unit);
                    verificationDataObj.addProperty("state", "");
                    verificationDataObj.addProperty("errorCode", electricityQueryResponse.getStatus());
                    verificationDataObj.addProperty("responseCode", "00");
                    verificationDataObj.addProperty("responseDesc", EnumResponseMsg.SUCCESS.toString());
                   // accessCode = electricityQueryResponse.getAccessCode();
                    redisService.setValue(accountOrMeterNo,electricityQueryResponse.getAccessCode());

                    log.info("============ the accesscode from redis is: "+redisService.getValue(accountOrMeterNo));

                    response.setCustomerName(electricityQueryResponse.getCustomer().getName());
                    response.setCustomerAddress(electricityQueryResponse.getCustomer().getAddress());
                    response.setBusinessUnit(EnumResponseMsg. business_unit);
                    response.setTariffDesc(electricityQueryResponse.getCustomer().getTariff());
                    response.setTariff(electricityQueryResponse.getCustomer().getTariffRate());
                    log.info(">>>>>>>>>> the tarif is: "+electricityQueryResponse.getCustomer().getTariffRate());
                    response.setState("");
                    response.setErrorCode(electricityQueryResponse.getStatus());
                    response.setResponseCode("00");
                    response.setResponseDesc(EnumResponseMsg.SUCCESS.toString());
                    ////////////////////////store the accescode in the session varaible
                   // accessCodeSingleton.setAccessCode(accountOrMeterNo, accessCode);


                    feeder = electricityQueryResponse.getCustomer().getFeeder_33_11_dt();
                    tariffRate = electricityQueryResponse.getCustomer().getTariffRate();
                    outStanding = electricityQueryResponse.getCustomer().getOutStanding();
                    faultparam = accessCode + "," + feeder + "," + tariffRate + "," + outStanding;
                    customername = electricityQueryResponse.getCustomer().getName() + "," + faultparam;
                    verificationDataObj.addProperty("customerName", customername);
                    verificationDataObj.addProperty("accountNumber", electricityQueryResponse.getCustomer().getAccountNumber());
                    verificationDataObj.addProperty("tariff", tariffRate);
                    verificationDataObj.addProperty("fault", faultparam);
                    verificationDataObj.addProperty("tariffCode", electricityQueryResponse.getCustomer().getTariff());
                    verificationDataObj.addProperty("phoneNumber", electricityQueryResponse.getCustomer().getPhone());




                } else {
                    verificationDataObj.addProperty("errorCode", electricityQueryResponse.getStatus());
                    verificationDataObj.addProperty("responseCode", "56");
                    verificationDataObj.addProperty("responseDesc", electricityQueryResponse.getMessage());

                    response.setErrorCode(electricityQueryResponse.getStatus());
                    response.setResponseCode("56");
                    response.setResponseDesc(electricityQueryResponse.getMessage());

                }
            }else{
                log.info("Unable to generate token");
                verificationDataObj.addProperty("errorCode", EnumResponseMsg.TOKENERROR.responseCode);
                verificationDataObj.addProperty("responseCode", EnumResponseMsg.TOKENERROR.responseCode);
                verificationDataObj.addProperty("responseDesc", EnumResponseMsg.TOKENERROR.responseMsg);

                response.setErrorCode(EnumResponseMsg.TOKENERROR.responseCode);
                response.setResponseCode(EnumResponseMsg.TOKENERROR.responseCode);
                response.setResponseDesc(EnumResponseMsg.TOKENERROR.responseMsg);
            }
        }catch (SocketTimeoutException e){
            log.info("Time out CustomerInfo Failure FOR POST PAID" + e);
            verificationDataObj.addProperty("errorCode", EnumResponseMsg.TIMEOUT.responseCode);
            verificationDataObj.addProperty("responseCode", EnumResponseMsg.TIMEOUT.responseCode);
            verificationDataObj.addProperty("responseDesc", EnumResponseMsg.TIMEOUT.responseMsg);

            response.setErrorCode(EnumResponseMsg.TIMEOUT.responseCode);
            response.setResponseCode(EnumResponseMsg.TIMEOUT.responseCode);
            response.setResponseDesc(EnumResponseMsg.TIMEOUT.responseMsg);

            log.info("POSTPAID CUSTOMER QUERY RESPONSE " + verificationDataObj.toString());
        }catch(Exception e){
            log.info("::::::::::::::the Exception  CustomerInfo Failure" + e);
            verificationDataObj.addProperty("errorCode", EnumResponseMsg.FAILED.responseCode);
            verificationDataObj.addProperty("responseCode", EnumResponseMsg.TIMEOUT.responseCode);
            verificationDataObj.addProperty("responseDesc", EnumResponseMsg.FAILED.responseMsg);

            response.setErrorCode(EnumResponseMsg.FAILED.responseCode);
            response.setResponseCode(EnumResponseMsg.TIMEOUT.responseCode);
            response.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);

            log.info("POSTPAID CUSTOMER QUERY RESPONSE " + verificationDataObj.toString());
        }
        result = verificationDataObj.toString();
        log.info("POSTPAID CUSTOMER QUERY RESPONSE:::::" + response);

        return response;
    }

    public ElectricityQueryResponse doPrepaidInfoPosting(ElectricityQueryRequest electricityQueryRequest){
        String clientRsp = null;
        String verifyUrl = null;
        String faultparam = null;
        String accessCode = null;
        String feeder = null;
        String tariffRate = null;
        String outStanding = null;
        String customername = null;

        ElectricityQueryResponse response = new ElectricityQueryResponse();
        log.info(":::::::::::::::::::::::::::PREPAID CUSTOMER QUERY REQUEST " + electricityQueryRequest);

        JsonObject verificationDataObj = new JsonObject();
        verificationDataObj.addProperty("requestType", "verification");
        verificationDataObj.addProperty("disco", "JOS");
        verificationDataObj.addProperty("accountType", "prepaid");
        verificationDataObj.addProperty("uniqueTransId", electricityQueryRequest.getReference());
        verificationDataObj.addProperty("accountNumber", electricityQueryRequest.getPayerId());

        response.setRequestType("verification");
        response.setDisco("JOS");
        response.setUniqueTransId(electricityQueryRequest.getReference());
        response.setAccountNumber(electricityQueryRequest.getPayerId());


        try {

//        generateAccessToken();
            // String token = jos.generateToken(tokenEnv, tokenUrl, adroitUser_d, adroitPass_d, adroitUser_l, adroitPass_l,logger);
            if (accessToken != null && accessToken.length() > 0) {
                String accountNumber = electricityQueryRequest.getPayerId().toString();
                log.info("Processing request...");

                QueryResponse queryResponse = jos.customerQuery(accountNumber, "prepaid", prepaidUrl, verifyEndpoint, accessToken, PRIVATE_KEY);

                String ref2 = "";

                if (queryResponse.getStatus().equals("100")) {

                    verificationDataObj.addProperty("errorCode", queryResponse.getStatus());
                    verificationDataObj.addProperty("email", "");
                    verificationDataObj.addProperty("phoneNumber", queryResponse.getCustomer().getPhone());
                    verificationDataObj.addProperty("minimumPurchase", "");
                    verificationDataObj.addProperty("customerArrears", "");
                    verificationDataObj.addProperty("tariffCode", queryResponse.getCustomer().getTariff());
                    verificationDataObj.addProperty("tariff", queryResponse.getCustomer().getTariffRate());
                    verificationDataObj.addProperty("accountNumber", queryResponse.getCustomer().getAccountNumber());
                    verificationDataObj.addProperty("customerAddress", queryResponse.getCustomer().getAddress());
                    verificationDataObj.addProperty("customerType", "PREPAID");
                    verificationDataObj.addProperty("businessUnit", EnumResponseMsg.business_unit);
                    verificationDataObj.addProperty("externalReference", ref2);
                    accessCode = queryResponse.getAccessCode();

                    response.setErrorCode(queryResponse.getStatus());
                    response.setTariff(queryResponse.getCustomer().getTariffRate());
                    response.setTariffDesc(queryResponse.getCustomer().getTariff());
                    response.setCustomerArrears("");
                    response.setCustomerAddress(queryResponse.getCustomer().getAddress());
                    response.setCustomerType("PREPAID");
                    response.setBusinessUnit(EnumResponseMsg.business_unit);
                    response.setExternalReference(ref2);
                    customername = queryResponse.getCustomer().getName();

                    //////////////////////////save the accesscode in the map
//                    accessCodeSingleton.setAccessCode(accountNumber, accessCode)
                    log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> the jedc accessCode set on: "+accessCode);
                    redisService.setValue(accountNumber,accessCode);
                    log.info("============ the accesscode from redis is: "+redisService.getValue(accountNumber));

                    feeder = queryResponse.getCustomer().getFeeder_33_11_dt();
                    tariffRate = queryResponse.getCustomer().getTariff();
                    outStanding = queryResponse.getCustomer().getOutStanding();
                    faultparam = accessCode + "," + feeder + "," + tariffRate + "," + outStanding;
                    //customername = queryResponse.getCustomer().getName() + "," + faultparam;
                    verificationDataObj.addProperty("customerName", customername);
                    //verificationDataObj.addProperty("fault", faultparam);
                    verificationDataObj.addProperty("responseCode", EnumResponseMsg.SUCCESS.responseCode);
                    verificationDataObj.addProperty("responseDesc", "Successful");

                    response.setCustomerName(customername);
                    response.setResponseCode(EnumResponseMsg.SUCCESS.responseCode);
                    response.setResponseDesc("Successful");
                } else {
                    verificationDataObj.addProperty("errorCode", queryResponse.getStatus());
                    verificationDataObj.addProperty("responseCode", "06");
                    verificationDataObj.addProperty("responseDesc", queryResponse.getMessage());

                    response.setErrorCode(queryResponse.getStatus());
                    response.setResponseCode("06");
                    response.setResponseDesc(queryResponse.getMessage());
                }

            } else {
                //log.error("Unable to generate token");
                log.info("Unable to generate token");
                verificationDataObj.addProperty("errorCode", EnumResponseMsg.TOKENERROR.responseCode);
                verificationDataObj.addProperty("responseCode", EnumResponseMsg.TOKENERROR.responseCode);
                verificationDataObj.addProperty("responseDesc", EnumResponseMsg.TOKENERROR.responseMsg);

                response.setErrorCode(EnumResponseMsg.TOKENERROR.responseCode);
                response.setResponseCode(EnumResponseMsg.TOKENERROR.responseCode);
                response.setResponseDesc(EnumResponseMsg.TOKENERROR.responseMsg);
            }
            clientRsp = verificationDataObj.toString();
            log.info("PREPAID CUSTOMER QUERY RESPONSE " + clientRsp);

        } catch (SocketTimeoutException e) {
            //log.error("Time out CustomerInfo Failure", e);
            log.info("Time out CustomerInfo Failure" + e.getMessage());

            verificationDataObj.addProperty("errorCode", EnumResponseMsg.TIMEOUT.responseCode);
            verificationDataObj.addProperty("responseCode", EnumResponseMsg.TIMEOUT.responseCode);
            verificationDataObj.addProperty("responseDesc", EnumResponseMsg.TIMEOUT.responseMsg);

            response.setErrorCode(EnumResponseMsg.TIMEOUT.responseCode);
            response.setResponseCode(EnumResponseMsg.TIMEOUT.responseCode);
            response.setResponseDesc(EnumResponseMsg.TIMEOUT.responseMsg);

            clientRsp = verificationDataObj.toString();
            log.info("PREPAID CUSTOMER QUERY RESPONSE " + clientRsp);
        } catch (Exception e) {
            //log.error("::::::::::::::the Exception  CustomerInfo Failure", e);
            log.info("::::::::::::::the Exception  CustomerInfo Failure" + e.getMessage());

            verificationDataObj.addProperty("errorCode", EnumResponseMsg.FAILED.responseCode);
            verificationDataObj.addProperty("responseCode", EnumResponseMsg.TIMEOUT.responseCode);
            verificationDataObj.addProperty("responseDesc", EnumResponseMsg.FAILED.responseMsg);

            response.setErrorCode(EnumResponseMsg.FAILED.responseCode);
            response.setResponseCode(EnumResponseMsg.TIMEOUT.responseCode);
            response.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);
            clientRsp = verificationDataObj.toString();
            log.info("PREPAID CUSTOMER QUERY RESPONSE " + clientRsp);
        }

        return response;
    }

    public ElectricityProcessResponse doPrepaidTransactionPosting(ElectricityProcessRequest electricityProcessRequest){

        log.info("PREPAID TRANSACTION POSTING REQUEST" + electricityProcessRequest);

        return customerPayment("PREPAID TRANSACTION POSTING REQUEST", electricityProcessRequest);
    }

    public ElectricityProcessResponse customerPayment(String action, ElectricityProcessRequest electricityProcessRequest) {

       // String response = "";
        JsonObject paymentDataObj = new JsonObject();
        JsonObject requestObject = new JsonObject();
        VerifyPayment verifyPayment = null;
        String fault = null;
        String accountNumber = null;
        String meterNumber = null;

        String units = null;
        String tarrifRate = null;
        String vat = null;
        String outstandingPaid = null;
        String tax = null;

        ElectricityProcessResponse response = new ElectricityProcessResponse();
        log.info("::::::::::::::::::::::::::::" + action + " TRANSACTION POSTING REQUEST::::" + electricityProcessRequest);

        try {
            // String token = jos.generateToken(tokenEnv, tokenUrl, adroitUser_d, adroitPass_d, adroitUser_l, adroitPass_l,logger);
            //generateAccessToken();

            if (token != null) {

                //
                String amount = String.valueOf(electricityProcessRequest.getAmount());
                //////////////////////////////////////////////get the access code from the map
                String accessCode = redisService.getValue(electricityProcessRequest.getPayerId()); ///accessCodeSingleton.getAccessCode(electricityProcessRequest.getPayerId());

                if(accessCode == null || accessCode.isEmpty()){

                    response.setErrorCode( EnumResponseMsg.INVALID_PARAMETERS.responseCode);
                    response.setResponseCode(EnumResponseMsg.INVALID_PARAMETERS.responseCode);
                    response.setResponseDesc(EnumResponseMsg.INVALID_PARAMETERS.responseMsg);

                    return  response;
                }
                log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>> the access code retrieve from the redis is: "+accessCode);

                log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> removing accesscode from redis");
                redisService.removeAccount(electricityProcessRequest.getPayerId());
                String uniqueTransId = electricityProcessRequest.getReference();
                String paymentChannel = electricityProcessRequest.getPaymentChannel();
                String mobile = electricityProcessRequest.getMobile();
                String channelCode = electricityProcessRequest.getReference().substring(0, 3);

                long unixTime = System.currentTimeMillis() / 1000L;
                String posted_on = Long.toString(unixTime);
//                  if (paymentChannel.startsWith("03") || paymentChannel.startsWith("05"))
//                  {
//                    mobileNo = "07080000000";
//                  }

                if (mobile.length() == 13 || mobile.length() == 11) {
                    if (mobile.length() == 13) {
                        mobile = "0" + mobile.substring(3, mobile.length());
                    } else {
                        mobile = "0" + mobile.substring(1, mobile.length());
                    }
                } else {
                    log.info(":::::::::::::::::::Invalid mobile number");
                    paymentDataObj.addProperty("errorCode", EnumResponseMsg.WORNG_MOBILE.responseCode);
                    paymentDataObj.addProperty("responseCode", EnumResponseMsg.WORNG_MOBILE.responseCode);
                    paymentDataObj.addProperty("responseDesc", EnumResponseMsg.WORNG_MOBILE.responseMsg);

                    response.setErrorCode(EnumResponseMsg.WORNG_MOBILE.responseCode);
                    response.setResponseCode(EnumResponseMsg.WORNG_MOBILE.responseCode);
                    response.setResponseDesc(EnumResponseMsg.WORNG_MOBILE.responseMsg);
                    return response;
                }

                paymentDataObj.addProperty("requestType", "payment");
                paymentDataObj.addProperty("disco", "JOS");
                paymentDataObj.addProperty("accountType", action);
                paymentDataObj.addProperty("uniqueTransId", uniqueTransId);
                paymentDataObj.addProperty("mobileNo", mobile);
                paymentDataObj.addProperty("tarfii",tarrifRate);

                response.setRequestType("payment");
                response.setDisco("JOS");
                response.setAccountType(action);
                response.setUniqueTransId(uniqueTransId);


                log.info(":::::::::::::::::::::::::calling " + action + " custompayment");
                PaymentResponse paymentResponse = jos.customPayment(accessCode, action, Double.parseDouble(amount), mobile, postpaidUrl, paymentEnpoint, accessToken, PRIVATE_KEY);

                if (paymentResponse.getStatus().equals("100")) {

                    log.info(":::::::::::::::::::::::calling verification endPoint after successful payment ");

                    verifyPayment = paymentVerification(paymentResponse.getAccessCode(), postpaidUrl, verifypayment, accessToken, PRIVATE_KEY);

                    if (!verifyPayment.getStatus().equals("100")) {
                        log.info(":::::::::::::::::::::::::::::the verification is unsuccessfull");

                        paymentDataObj.addProperty("errorCode", verifyPayment.getStatus());
                        paymentDataObj.addProperty("responseCode", "06");
                        paymentDataObj.addProperty("responseDesc", verifyPayment.getMessage());

                        response.setErrorCode(verifyPayment.getStatus());
                        response.setResponseCode("06");
                        response.setResponseDesc(verifyPayment.getMessage());
                        return response;
                    }

                    log.info(":::::::::::::::::::::::::::::the verification is successfull");
                    accountNumber = verifyPayment.getPayDetails().getAccountNumber();
                    meterNumber = verifyPayment.getPayDetails().getMeterNumber();
                    token = verifyPayment.getPayDetails().getToken();
                    units = verifyPayment.getPayDetails().getUnits();
                    tarrifRate = verifyPayment.getPayDetails().getTariffRate();
                    vat = verifyPayment.getPayDetails().getVat();
                    outstandingPaid = verifyPayment.getPayDetails().getOutstandingPaid();

                    MainTokenData tokenData = new MainTokenData();

                    tokenData.setUnit(action.equalsIgnoreCase("POSTPAI")?null:units);
                    tokenData.setAmount(verifyPayment.getPayDetails().getAmount());
                    tokenData.setVat(vat);
                    tokenData.setFixedCharge(tarrifRate);
                    tokenData.setToken(token);

                    response.setMainToken(tokenData);

                    log.info("::::::::::::::::::::::::the values inside the map is: "+accessCodeSingleton.getAccessCode(electricityProcessRequest.getPayerId()));

                    accessCodeSingleton.removeAccessCode(electricityProcessRequest.getPayerId());

                    log.info("::::::::::::::::::::::::the values inside the map after removing the accesscode is: "+accessCodeSingleton.getAccessCode(electricityProcessRequest.getPayerId()));

                    log.info("::::::::::::::::::::::::::the size of JEDC map that stores account and accessCode is: "+accessCodeSingleton.getSize());

                    fault = meterNumber + "," + token + "," + units + "," + tarrifRate + "," + vat + "," + outstandingPaid;

                    paymentDataObj.addProperty("externalReference", verifyPayment.getPayDetails().getTransactionID());
                    paymentDataObj.addProperty("responseCode", "00");
                    paymentDataObj.addProperty("responseDesc", "Successful");
                    paymentDataObj.addProperty("businessUnit", "JOS BUSINESS UNIT");
                    paymentDataObj.addProperty("accountNumber", accountNumber);
                    paymentDataObj.addProperty("fault", fault);

                    response.setExternalReference(verifyPayment.getPayDetails().getTransactionID());
                    response.setResponseCode("00");
                    response.setResponseDesc("Successful");
                    response.setBusinessUnit("JOS BUSINESS UNIT");
                    response.setAccountNumber(accountNumber);
                    response.setFault(fault);
                    return response;
                } else {
                    log.info(":::::::::::::::::::::::::::the payment is unsuccessful");
                    String message = "";
                    if (paymentResponse.getMessage() == null || "".equals(paymentResponse.getMessage())) {
                        message = "Transaction Failed";
                    } else {
                        message = paymentResponse.getMessage();
                    }

                    paymentDataObj.addProperty("errorCode", paymentResponse.getStatus());
                    paymentDataObj.addProperty("responseCode", "06");
                    paymentDataObj.addProperty("responseDesc", message);

                    response.setErrorCode(paymentResponse.getStatus());
                    response.setResponseCode("06");
                    response.setResponseDesc(message);

                }
            } else {
                log.info("Unable to generate token");
                paymentDataObj.addProperty("errorCode", EnumResponseMsg.TOKENERROR.responseCode);
                paymentDataObj.addProperty("responseCode", EnumResponseMsg.TOKENERROR.responseCode);
                paymentDataObj.addProperty("responseDesc", EnumResponseMsg.TOKENERROR.responseMsg);


                response.setErrorCode( EnumResponseMsg.TOKENERROR.responseCode);
                response.setResponseCode(EnumResponseMsg.TOKENERROR.responseCode);
                response.setResponseDesc(EnumResponseMsg.TOKENERROR.responseMsg);
            }
        } catch (SocketTimeoutException e) {
            //log.error("Time out CustomerInfo Failure FOR POST PAID", e);
            log.info("::::::::::::::::::::::::::Time out for" + action + " " + e);

            paymentDataObj.addProperty("errorCode", EnumResponseMsg.TIMEOUT.responseCode);
            paymentDataObj.addProperty("responseCode", EnumResponseMsg.TIMEOUT.responseCode);
            paymentDataObj.addProperty("responseDesc", EnumResponseMsg.TIMEOUT.responseMsg);

            response.setErrorCode( EnumResponseMsg.TIMEOUT.responseCode);
            response.setResponseCode(EnumResponseMsg.TIMEOUT.responseCode);
            response.setResponseDesc(EnumResponseMsg.TIMEOUT.responseMsg);

            log.info("::::::::::::::::::::::::::" + action + " payment Error RESPONSE " + paymentDataObj.toString());
        } catch (Exception e) {
            //log.error("::::::::::::::the Exception  CustomerInfo Failure", e);
            log.info(":::::::::::::::::::::::the Exception  CustomerInfo Failure" + e);

            paymentDataObj.addProperty("errorCode", EnumResponseMsg.FAILED.responseCode);
            paymentDataObj.addProperty("responseCode", EnumResponseMsg.FAILED.responseCode);
            paymentDataObj.addProperty("responseDesc", EnumResponseMsg.FAILED.responseMsg);

            response.setErrorCode( EnumResponseMsg.FAILED.responseCode);
            response.setResponseCode(EnumResponseMsg.FAILED.responseCode);
            response.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);

            log.info("::::::::::::::::::::::::::::::" + action + " RESPONSE " + paymentDataObj.toString());
        }
   //     response = paymentDataObj.toString();

        log.info("::::::::::::::::::::::::::::::" + action + " RESPONSE for jedc" + paymentDataObj.toString());

        return response;
    }

    public VerifyPayment paymentVerification(String accessCode, String baseUrl, String paymentVerificationEnpoint, String accessToken, String PRIVATE_KEY) throws Exception {
        VerifyPayment verifyPayment = null;
        log.info(":::::::::::::::the acceess code used for paymentVerfication is: " + accessCode);
        log.info(":::::::::::::::::>>>>>>>>>>>>>>>>>>>>calling verifyPayment with the following parameters: accessCode " + accessCode + " baseurl: " + baseUrl + " verifyendpoint: " + paymentVerificationEnpoint + " token " + accessToken + " privatekey " + PRIVATE_KEY);
        verifyPayment = jos.verifyPayment(accessCode, baseUrl, paymentVerificationEnpoint, accessToken, PRIVATE_KEY);
        log.info(":::::::::::::::::::::the response for verify payment for Jos Electricity is: " + verifyPayment.toString());
        return verifyPayment;
    }

    public ElectricityProcessResponse doPostPaidTransactionPosting(ElectricityProcessRequest electricityProcessRequest){

        log.info("::::::::::::::::::::::POSTPAID TRANSATION POSTING REQUEST" + electricityProcessRequest);

        return customerPayment("POSTPAID TRANSACTION POSTING REQUEST", electricityProcessRequest);
    }

//    public ElectricityProcessResponse doPostPaidTransactionReversal(ElectricityReProcessRequest electricityReProcessRequest){
//
//        String uniqueTransId = electricityReProcessRequest.getUniqueTransId();
//
//        JsonObject reversalDataObj = new JsonObject();
//        String result = "";
//        reversalDataObj.addProperty("requestType", "reversal");
//        reversalDataObj.addProperty("disco", "JOS");
//        reversalDataObj.addProperty("accountType", "postpaid");
//        reversalDataObj.addProperty("responseCode", "00");
//        reversalDataObj.addProperty("responseDesc", "SUCCESSFUL");
//
//        result = reversalDataObj.toString();
//
//        return new ElectricityProcessResponse();
//    }
}
