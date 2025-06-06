package com.lemonpay.lemonpayvas.electricity.ikedc.core;

import com.lemonpay.lemonpayvas.electricity.ikedc.dto.response.*;
import com.lemonpay.lemonpayvas.electricity.phcnnode.PHCNNode;
import com.lemonpay.lemonpayvas.electricity.phcnnode.dto.*;
import com.lemonpay.lemonpayvas.electricity.redisutility.redisservice.NewVasgateRedisService;
import com.lemonpay.lemonpayvas.electricity.ikedc.actionMenu.Action;
import com.lemonpay.lemonpayvas.electricity.ikedc.dto.request.ValidationReqDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

@Service
@Slf4j
@Component("phcnikj")
public class IKEDCProcessor extends PHCNNode {

    @Autowired
    NewVasgateRedisService redisService;

    static Properties properties;

  //  private static String redisUrl;

    private static String runningMode;

    private static String endpoint = "";

    private String district;

    private static String merchantReference;

    Map<String, String> channelNames = null;

 //   private static Logger logger = null;

    AuthResDto authRestDto = null;

    static IKEDCEncPinGenerator iKEDCEncPinGenerator = new IKEDCEncPinGenerator();

    private static String baseUrl;

    private static Properties prop;

    private static String walletId;

    private static String password;

    private static String username;

    private static String identifier;

    private static String verifyEndpoint;
    private static String requeryeNDPOINT;

    private static String IKEDC_PAYMENTENDPOINT;

    private static String authEndpoint;

    private static String encpinEndpoint;

    private static String requestSignature_key;

    private static String payvicepin;

    private static String token;
    private static String defaultNumber;

    private static AccountQueryProcessor accountQueryProcessor;

    private static ChannelMap channelMap;

    private static String IKEDC_CHANNEL;

    private static String IKEDC_BUSINESS_ID;
    private static String PRODUCT_ID;
    private static String meterNo;

    static {
     //   logger = null;
        prop = new Properties();
        try {
            prop.load(new FileInputStream(new File("/resources/application.properties")));
         //   redisUrl = prop.getProperty("REDIS_URL");
            baseUrl = prop.getProperty("IKEDC_BASEURL");
            token = prop.getProperty("IKEDC_SIGNATURE_KEY");
            defaultNumber = prop.getProperty("IKEDC_DEFAULT_NO");
            accountQueryProcessor = new AccountQueryProcessor();
            verifyEndpoint = prop.getProperty("IKEDC_VERIFYENDPOINT");
            IKEDC_BUSINESS_ID = prop.getProperty("IKEDC_BUSINESS_ID");
            PRODUCT_ID = prop.getProperty("IKEDC_PRODUCT_ID");
            IKEDC_PAYMENTENDPOINT = prop.getProperty("IKEDC_PAYMENTENDPOINT");
            requeryeNDPOINT = prop.getProperty("IKEDC_REQUERY_ENDPOINT");
            meterNo = prop.getProperty("IKEDC_METER_NUMBER");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String param(String s) {
        String val = null;
        try {
            val = (String) properties.get(s);
        } catch (Exception exception) {
        }
        return val;
    }

    @Override
    public ElectricityQueryResponse query(ElectricityQueryRequest electricityQueryRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>the request sent for ikeja electricity: "+ objectMapper.writeValueAsString(electricityQueryRequest));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if(electricityQueryRequest.getType().equalsIgnoreCase(Action.POSTPAID.toString())){
            return  doPostPaidCustomerInfo(electricityQueryRequest);
        }else if(electricityQueryRequest.getType().equalsIgnoreCase(Action.PREPAID.toString())){

            return  doPrepaidCustomerInfo(electricityQueryRequest);
        }
        return null;
    }

    private ElectricityQueryResponse doPrepaidCustomerInfo(ElectricityQueryRequest electricityQueryRequest) {
        log.info("{}:::::::::::::::::::::::::ENTERING IKEDC doPrepaidCustomerInfo METHOD WITH" + electricityQueryRequest.toString());
         customQuery("PREPAID", electricityQueryRequest);
         return new ElectricityQueryResponse();
    }

    public ElectricityProcessResponse customePayment(String action, ElectricityProcessRequest electricityProcessRequest) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>" + action + " TRANSACTION POSTING REQUEST:::::" + electricityProcessRequest.toString());
        String output = "";
        LocalDate localDate = LocalDate.now();

        int min = 100000; // Smallest 6-digit number
        int max = 999999; // Largest 6-digit number
        Random rand = new Random();
        Object kctArray = "";

        PaymentResDto paymentResDto = null;

        String yyyy = String.valueOf(localDate.getYear());
        String mm = String.valueOf(localDate.getMonthValue()).length() < 2 ? "0" + localDate.getMonthValue() : String.valueOf(localDate.getMonthValue());
        String dd = String.valueOf(localDate.getDayOfMonth()).length() < 2 ? "0" + localDate.getDayOfMonth() : String.valueOf(localDate.getDayOfMonth());
        String generatedRandom = String.valueOf(rand.nextInt(max - min + 1) + 100000);
        String generatedRef = yyyy + mm + dd + generatedRandom + IKEDC_BUSINESS_ID;

        ElectricityProcessResponse paymentDataObj = new ElectricityProcessResponse();
        VendResponseDto vendResponseDto = null;
        VendFailed vendFailed = null;
        Gson gson = new Gson();
        String reference = electricityProcessRequest.getReference();

        Double amount = Double.parseDouble(String.valueOf(electricityProcessRequest.getAmount()));
        String mobile = electricityProcessRequest.getMobile();
        String meterNo = electricityProcessRequest.getPayerId();
        String paymentChannel = electricityProcessRequest.getPaymentChannel();
        String disco = "IKEJA";
        String vendType = action.toLowerCase();

        String tokenResponse = null;
        String units = null;
        String tariff = null;
        String tariffName = null;
        String tax = null;
        String debtRemaining = null;

        String fault = null;
        String freeUnits = null;
        String balance = null;
        String lor = null;
        String penalty = null;
        String serviceCharge = null;
        String installationFee = null;
        String adminCharge = null;
        String replacementCost = null;
        String reconnectionFee = null;
        String vat = null;
        String costOfUnit = null;

        if (!mobile.isEmpty()) {
            log.info("::::::::THIS IS THE MOBILE NUM USED:::::::" + mobile);
            if (mobile.length() == 13 || mobile.length() == 11) {
                if (mobile.length() == 13) {
                    if (mobile.startsWith("234")) {
                        mobile = "0" + mobile.substring(3, mobile.length());
                    } else {
                        mobile = defaultNumber;
                    }

                } else {
                    if (!mobile.startsWith("0")) {
                        mobile = defaultNumber;
                    } else {
                        if (mobile.startsWith("04")) {
                            mobile = defaultNumber;
                        } else if (mobile.startsWith("02")) {
                            mobile = defaultNumber;
                        } else if (mobile.startsWith("03")) {
                            mobile = defaultNumber;
                        } else if (mobile.startsWith("05")) {
                            mobile = defaultNumber;
                        } else if (mobile.startsWith("06")) {
                            mobile = defaultNumber;
                        } else if (mobile.startsWith("00")) {
                            mobile = defaultNumber;
                        }
                    }
                }
            } else {
                mobile = defaultNumber;
            }
        } else {
            mobile = defaultNumber;
        }

//        if(paymentChannel.equalsIgnoreCase("05")){
//            mobile = defaultNumber;
//        }
        log.info(":::::mobile number:::::: " + mobile);
        System.out.println(":::::mobile number:::::: " + mobile);
        paymentDataObj.setExternalReference(generatedRef);
        paymentDataObj.setAmount(String.valueOf(amount));
        paymentDataObj.setAccountNumber(meterNo);
        paymentDataObj.setRequestType(vendType);
        paymentDataObj.setAccountType("NMD");
        paymentDataObj.setProductId(PRODUCT_ID);
        paymentDataObj.setMobile(mobile);
        PaymentNotification paymentNotifiaction = new PaymentNotification();
        String strPayLoad = gson.toJson(paymentDataObj);
        String customerAcctType = "";
        try {


           // RedisService redisService = new RedisService(redisUrl);
            try {
                customerAcctType = redisService.getValue(meterNo);
                redisService.removeAccount(meterNo);
                log.info("REDIS INFO FOR METER " + meterNo + " is::: " + customerAcctType);
            } catch (Exception ex) {
                log.error("*********Could not find customer on redis:: " + ex);
                paymentDataObj.setErrorCode("06");
                paymentDataObj.setResponseCode("06");
                paymentDataObj.setResponseDesc("Transaction cannot be completed:: Please do query first");
                return paymentDataObj;
            }
            if (customerAcctType == null || customerAcctType.isEmpty()) {
                paymentDataObj.setErrorCode("06");
                paymentDataObj.setResponseCode("06");
                paymentDataObj.setResponseDesc("Transaction cannot be completed:: Please do query first");
                return paymentDataObj;
            }

            if (customerAcctType.equalsIgnoreCase("MD")) {
                log.info("Skipping payment processing for MD accounts.");
                paymentDataObj.setErrorCode("06");
                paymentDataObj.setResponseCode("06");
                paymentDataObj.setResponseDesc("Transaction cannot be completed or processed for MD accounts");

                return paymentDataObj;
            }
            paymentResDto = paymentNotifiaction.vendMeter(strPayLoad, vendType, baseUrl, IKEDC_PAYMENTENDPOINT, token);

            log.info(">>>>>>>>>>>>>>>>>>>>>>>> the response from payment notification is " + gson.toJson(paymentResDto));
            if (paymentResDto.getStatus() == 200) {
                log.info(":::::::::::::::::::::::::::::the " + vendType + " payment notification was successfull");
                //vendResponseDto = (VendResponseDto) gson.fromJson(outcome[1], VendResponseDto.class);
                //tokenResponse = vendResponseDto.getData().getToken();

                units = paymentResDto.getData().getUnits();
                tariffName = paymentResDto.getData().getTariffName();
                tariff = paymentResDto.getData().getRate();
                tokenResponse = paymentResDto.getData().getToken();
                tax = "0";
                if (action.equalsIgnoreCase("prepaid")) {
                    debtRemaining = paymentResDto.getData().getList().getIndex().get(0).getFeeAmount();
                    reconnectionFee = paymentResDto.getData().getList().getIndex().get(2).getFeeAmount();
                    lor = paymentResDto.getData().getList().getIndex().get(3).getFeeAmount();
                    penalty = paymentResDto.getData().getList().getIndex().get(8).getFeeAmount();
                    serviceCharge = paymentResDto.getData().getList().getIndex().get(9).getFeeAmount();
                    installationFee = paymentResDto.getData().getList().getIndex().get(7).getFeeAmount();
                    adminCharge = paymentResDto.getData().getList().getIndex().get(6).getFeeAmount();
                    vat = paymentResDto.getData().getList().getIndex().get(4).getFeeAmount();
                    costOfUnit = paymentResDto.getData().getList().getIndex().get(5).getFeeAmount();

                    log.info("debtremaing " + debtRemaining + " reconnection fee " + reconnectionFee + " lor " + lor
                            + " penalty " + penalty + " servicecharge " + serviceCharge + " installationFee " + installationFee +
                            "adminCharge " + adminCharge + " vat " + vat + " costOfUnit " + costOfUnit);

                    kctArray = paymentResDto.getData().getKct().equals("") ? "" : paymentResDto.getData().getKct();

                    //replacementCost =

                } else {
                    debtRemaining = paymentResDto.getData().getRemaininDept();
                }

                balance = paymentResDto.getData().getBalance();

                freeUnits = "0";
                MainTokenData maintoken = new MainTokenData();
                maintoken.setUnit(units);
                maintoken.setAmount(String.valueOf(amount));
                maintoken.setVat(vat);
                maintoken.setToken(tokenResponse);
                maintoken.setFixedCharge(" ");


                //String kctArray = paymentResDto.getData().getKct();


//                     maintoken.addProperty("kctToken",   doesFieldKctExist(paymentResDto, "kct")==true? String.join(", ", paymentResDto.getData().getKct()) : "");
                //.addProperty("kctToken",   paymentResDto.getData().getKct()!=null? String.join(", ", paymentResDto.getData().getKct()) : "");


                fault = meterNo + "," + tokenResponse + "," + units + "," + tariff + "," + tax + "," + debtRemaining;
                paymentDataObj.setMainToken(maintoken);
                paymentDataObj.setExternalReference(paymentResDto.getData().getReceiptNO());
                paymentDataObj.setResponseCode("00");
                paymentDataObj.setErrorCode("00");
                paymentDataObj.setUnitsPurchased(units);
                paymentDataObj.setResponseDesc("Success");
                paymentDataObj.setBusinessUnit("IKJ BUSINESS UNIT");
                paymentDataObj.setAccountNumber(meterNo);
                paymentDataObj.setFault(fault);
                paymentDataObj.setExternalReference(reference);
                paymentDataObj.setCustomerArrears(debtRemaining);
                paymentDataObj.setBalance(balance);
                paymentDataObj.setTariff(tariff);
                paymentDataObj.setTariffDescription(paymentResDto.getData().getTariffName());
                paymentDataObj.setMainToken(kctArray.equals("") ? null : getKCTToken(((List<String>) kctArray).toArray(new String[0])));//////
                paymentDataObj.setReconnectionFee(reconnectionFee);
                paymentDataObj.setLossOfFee(lor);
                paymentDataObj.setUnitsPurchased(costOfUnit);
                paymentDataObj.setPenalty(penalty);
                paymentDataObj.setServiceCharge(serviceCharge);
                paymentDataObj.setInstallationFee(installationFee);
                paymentDataObj.setAdminCharge(adminCharge);

                log.info(">>>>>>>>>>>>>>>>>>>> the payment object is: " + paymentDataObj);
            } else if (paymentResDto.getStatus() == 202) {

                return requery(reference);
            } else {
                log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> the payment error: " + paymentResDto.getMessage());
                paymentDataObj.setErrorCode("06");
                paymentDataObj.setResponseCode("06");
                paymentDataObj.setResponseDesc(paymentResDto.getMessage());
                paymentDataObj.setResponseDesc(paymentResDto.getMessage());
                paymentDataObj.setResponseDesc(paymentResDto.getMessage());
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            log.info("Time out doPostPaidTransactionPosting Failure error is::::::::::::::: " + e);
            paymentDataObj.setErrorCode(EnumResponseMsg.TIMEOUT.responseCode);
            paymentDataObj.setResponseCode(EnumResponseMsg.TIMEOUT.responseCode);
            paymentDataObj.setResponseDesc("Error occurred:: Transaction Failed..");
            // paymentDataObj.addProperty("error", paymentResDto.getMessage());
        } catch (Exception e) {

            log.info("::::::::::::::the Exception  doPostPaidTransactionPosting Failure error is :::::::" + e.getMessage());
            System.out.println("::::::::::::::the Exception  doPostPaidTransactionPosting Failure error is :::::::" + e.getMessage());
            paymentDataObj.setErrorCode(EnumResponseMsg.FAILED.responseCode);
            paymentDataObj.setResponseCode(EnumResponseMsg.FAILED.responseCode);
            paymentDataObj.setResponseDesc("Error occurred:: Transaction Failed");
            //   paymentDataObj.addProperty("error", paymentResDto.getMessage());
        }
        String result = paymentDataObj.toString();
        log.info("... Leaving doPostPaidTransactionPosting Method ...");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> the transaction response from ikeja to phcnnode : " + result);

        return paymentDataObj;
    }

    public ElectricityQueryResponse  customQuery(String action, ElectricityQueryRequest electricityQueryRequest) {
        log.info("{}:::::::::::::::::::::::::" + action + " CUSTOMER INFO QUERY REQUEST:::::" + electricityQueryRequest.toString());
        ElectricityQueryResponse electricityQueryResponse = new ElectricityQueryResponse();
        Gson gson = new Gson();
        QueryFailed queryFailed = null;
        QuerySuccess querySuccess = null;
        String faultparam = null;
        String meterNo = electricityQueryRequest.getPayerId();
        String uniqueTransId = electricityQueryRequest.getReference();
        String vendType = action.toLowerCase();
        Boolean orderId = Boolean.valueOf(false);
        ValidationResDto validationResDto = null;
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> create ikeja query request");
        ValidationReqDto validationReqDto = new ValidationReqDto();
        validationReqDto.setRequestNumber(meterNo);
        validationReqDto.setType(vendType);
        String stringReq = gson.toJson(validationReqDto);
        String externalRef = " ";
        electricityQueryResponse.setRequestType("verification");
        electricityQueryResponse.setDisco("IKJ");
        electricityQueryResponse.setCustomerType(vendType);
        electricityQueryResponse.setAccountNumber(meterNo);
        electricityQueryResponse.setUniqueTransId(uniqueTransId);
        electricityQueryResponse.setExternalReference(externalRef);
        try {
            validationResDto = accountQueryProcessor.checkMeter(meterNo, vendType, stringReq, baseUrl, verifyEndpoint, token);

            if (validationResDto.isSuccess()) {

             //   RedisService redisService = new RedisService();

                redisService.setValue(meterNo, validationResDto.getData().getResponse().getAccountType());

                String accountType = validationResDto.getData().getResponse().getAccountType();

                if (accountType.equalsIgnoreCase("MD")) {
                    log.info("Skipping query for MD accounts.");
                    electricityQueryResponse.setErrorCode("56");
                    electricityQueryResponse.setResponseCode("56");
                    electricityQueryResponse.setResponseDesc("Transaction cannot be completed or processed for MD accounts");

                //    String result = electricityQueryResponse.toString();
                    return electricityQueryResponse;
                }
                electricityQueryResponse.setCustomerType(validationResDto.getData().getResponse().getAccountType());
//                querySuccess = (QuerySuccess) gson.fromJson(outcome[1], QuerySuccess.class);
                electricityQueryResponse.setCustomerName(validationResDto.getData().getResponse().getName());
                electricityQueryResponse.setCustomerAddress(validationResDto.getData().getResponse().getServiceAddress());
                electricityQueryResponse.setBusinessUnit("IKJ business unit");
                electricityQueryResponse.setTariffCode(validationResDto.getData().getResponse().getTariffName());
                electricityQueryResponse.setTariff(doesFieldtarifExist(validationResDto, "tariff") == false ? "0.0" : validationResDto.getData().getResponse().getTariff());
                electricityQueryResponse.setState("");
                electricityQueryResponse.setEmail("");
                electricityQueryResponse.setErrorCode("00");
                electricityQueryResponse.setResponseCode("00");
                electricityQueryResponse.setResponseDesc("Successful");
                electricityQueryResponse.setCustomerArrears(validationResDto.getData().getResponse().getOutstandingDebt());
                electricityQueryResponse.setMinimumPurchase(validationResDto.getData().getResponse().getMinimumVend() == null ? "0.0" : validationResDto.getData().getResponse().getMinimumVend());
                electricityQueryResponse.setMaxPurchase("0");
                //verificationDataObj.addProperty("debtRepayment", querySuccess.getDebtRepayment());
                faultparam = "tariff:" + 0 + ",tariffDesc:" + "" + ",tariffClass:" + "";
            //    electricityQueryResponse.addProperty("fault", faultparam);
            } else if (!validationResDto.isSuccess()) {

                electricityQueryResponse.setErrorCode("56");
                electricityQueryResponse.setResponseCode("56");
                electricityQueryResponse.setResponseDesc(validationResDto.getMessage());
            }
        } catch (SocketTimeoutException e) {
            log.error("Time out CustomerInfo Failure FOR POSTPAID" + e);
            electricityQueryResponse.setErrorCode(EnumResponseMsg.TIMEOUT.responseCode);
            electricityQueryResponse.setResponseCode(EnumResponseMsg.TIMEOUT.responseCode);
            electricityQueryResponse.setResponseDesc(EnumResponseMsg.TIMEOUT.responseMsg);
        } catch (Exception e) {
            log.error("::::::::::::::the Exception  CustomerInfo Failure" + e.getMessage());
            electricityQueryResponse.setErrorCode(EnumResponseMsg.FAILED.responseCode);
            electricityQueryResponse.setResponseCode(EnumResponseMsg.TIMEOUT.responseCode);
            electricityQueryResponse.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);
        }
        String result = electricityQueryResponse.toString();
        log.info("::::::::::::::::::::::::::::::::::::: CUSTOMER QUERY RESPONSE POSTPAID:::::" + result);
        System.out.println("::::::::::::::::::::::::::::::::::::: CUSTOMER QUERY RESPONSE POSTPAID:::::" + result);
        return electricityQueryResponse;
    }

    private ElectricityQueryResponse doPostPaidCustomerInfo(ElectricityQueryRequest electricityQueryRequest) {
        return null;
    }

    public boolean doesFieldtarifExist(ValidationResDto validationResDto, String tariff) {

        Class<?> object = validationResDto.getClass();

        try {
            Field field = object.getField(tariff);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public MainTokenData getKCTToken(String[] arr) {

//        JsonObject kctToken = new JsonObject();
//
//        kctToken.addProperty("kct1", arr[0]);
//        kctToken.addProperty("kct2", arr[1]);
//
//        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + kctToken);

        MainTokenData tokenData = new MainTokenData();

            tokenData.setKctTokens(arr[0]);
            tokenData.setKctTokens(arr[1]);

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + tokenData);

        return tokenData;
    }

    public AuthResDto getAuthToken(String walletId, String username, String password, String identifier, String baseUrl, String authEndpoint) throws Exception {
        Long currentTime = Long.valueOf(System.currentTimeMillis());
        if (this.authRestDto != null && currentTime.longValue() - this.authRestDto.getData().getCreatTime().longValue() > 6600000L)
            return this.authRestDto;
        IKEDCAuthTokenUtil iKEDCAuthTokenUtil = new IKEDCAuthTokenUtil();
        AuthResDto authResDto = iKEDCAuthTokenUtil.getToken(walletId, username, password, identifier, baseUrl, authEndpoint);
        log.info("{}::::::::::::::::::::::::::::::the token gotten is: " + authResDto.getData().getApiToken());
        authResDto.getData().setCreatTime(currentTime);
        return authResDto;
    }

    public ElectricityProcessResponse requery(String reference) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> calling transaction requery");

        PaymentResData paymentResDto = null;
        Gson gson = new Gson();
        ElectricityProcessResponse paymentDataObj = new ElectricityProcessResponse();

        String disco = "ikeja";

        String tokenResponse = null;
        String units = null;
        String tariff = null;
        String tax = null;
        String accountType = null;
        String debtRemaining = null;
        String reconnectionFee = null;
        String fault = null;
        String freeUnits = null;
        String lor = null;
        String meterNo = null;
        String penalty = null;
        String serviceCharge = null;
        String installationFee = null;
        String adminCharge = null;
        String vat = null;
        String costOfUnit = null;
        String balance = null;

        ReQueryResponseDto reQueryResponseDto = null;
        ReQueryResponseData requeryResData = null;


        PaymentNotification paymentNotifiaction = new PaymentNotification();
        int count = 0;
        try {

            do {
                reQueryResponseDto = accountQueryProcessor.requery(reference, baseUrl, requeryeNDPOINT, token);
                count++;

                log.info(">>>>>>>>>>>>>>>>>>>>>>>> the response from requery for the " + count + " is" + gson.toJson(reQueryResponseDto));

            } while ((reQueryResponseDto == null || reQueryResponseDto.getCode() == 202 || reQueryResponseDto.getData().getTransactionStatus().equalsIgnoreCase("pending")) && count <= 2);

            if (reQueryResponseDto != null && reQueryResponseDto.isSuccess()) {
                log.info(":::::::::::::::::::::::::::::the postpaid payment notification was successfull");
                //vendResponseDto = (VendResponseDto) gson.fromJson(outcome[1], VendResponseDto.class);
                //tokenResponse = vendResponseDto.getData().getToken();
                requeryResData = reQueryResponseDto.getData();

                units = requeryResData.getUnits();
                tariff = requeryResData.getTariffName();
                tokenResponse = requeryResData.getToken();
                accountType = requeryResData.getAccountType();
                meterNo = requeryResData.getMeterNO();
                Double amount = requeryResData.getAmountVend();
                tax = "0";

                if (accountType.equalsIgnoreCase("prepaid")) {
                    debtRemaining = requeryResData.getRemainingDept();
                    reconnectionFee = requeryResData.getReconnectionFee();
                    lor = requeryResData.getLossofRevenue();
                    penalty = requeryResData.getPenalty();
                    serviceCharge = requeryResData.getServiceCharge();
                    installationFee = requeryResData.getInstallationFee();
                    adminCharge = requeryResData.getAdministrativeCharge();
                    vat = requeryResData.getVat();
                    costOfUnit = requeryResData.getCostOfUnits();

                    log.info("debtremaing " + debtRemaining + " reconnection fee " + reconnectionFee + " lor " + lor
                            + " penalty " + penalty + " servicecharge " + serviceCharge + " installationFee " + installationFee +
                            "adminCharge " + adminCharge + " vat " + vat + " costOfUnit " + costOfUnit);

                    //replacementCost =

                } else {
                    debtRemaining = requeryResData.getRemainingDept();
                }

                balance = requeryResData.getBalance();
                freeUnits = "0";
                MainTokenData maintoken = new MainTokenData();
                maintoken.setUnit(units);
                maintoken.setAmount(String.valueOf(amount));
                maintoken.setVat(vat);
                maintoken.setToken(tokenResponse);
                maintoken.setFixedCharge(" ");

                //String[] kctArray = paymentResDto.getDa

                maintoken.setKctTokens(String.join(", ", (String[]) paymentResDto.getKct()));

                fault = meterNo + "," + tokenResponse + "," + units + "," + tariff + "," + tax + "," + debtRemaining;
                paymentDataObj.setMainToken(maintoken);
                paymentDataObj.setExternalReference(requeryResData.getReceiptNO());
                paymentDataObj.setResponseCode("00");
                paymentDataObj.setErrorCode("00");
                paymentDataObj.setUnitsPurchased(units);
                paymentDataObj.setResponseDesc("Success");
                paymentDataObj.setBusinessUnit("IKJ BUSINESS UNIT");
                paymentDataObj.setAccountNumber(meterNo);
          //      paymentDataObj.addProperty("fault", fault);
                paymentDataObj.setExternalReference(reference);
                paymentDataObj.setCustomerArrears(debtRemaining);
                paymentDataObj.setBalance(balance);
                paymentDataObj.setTariff(tariff);
                paymentDataObj.setTariffDescription(requeryResData.getTariffName());
                paymentDataObj.setMainToken(requeryResData.getKct() != null ? getKCTToken(requeryResData.getKct()) : null);
                paymentDataObj.setReconnectionFee(reconnectionFee);
                paymentDataObj.setLossOfFee(lor);
                paymentDataObj.setUnitsPurchased(costOfUnit);
                paymentDataObj.setPenalty(penalty);
                paymentDataObj.setServiceCharge(serviceCharge);
                paymentDataObj.setInstallationFee(installationFee);
                paymentDataObj.setAdminCharge(adminCharge);
            } else {

                paymentDataObj.setErrorCode("05");
                paymentDataObj.setResponseCode("05");
                paymentDataObj.setResponseDesc(paymentResDto.getMessage());
                paymentDataObj.setResponseDesc(paymentResDto.getMessage());
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            log.info("Time out doPostPaidTransactionPosting Failure error is::::::::::::::: " + e);
            paymentDataObj.setErrorCode(EnumResponseMsg.TIMEOUT.responseCode);
            paymentDataObj.setResponseCode(EnumResponseMsg.TIMEOUT.responseCode);
            paymentDataObj.setResponseDesc(EnumResponseMsg.TIMEOUT.responseMsg);
            paymentDataObj.setResponseDesc(paymentResDto.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("::::::::::::::the Exception  doPostPaidTransactionPosting Failure error is :::::::" + e.getMessage());
            paymentDataObj.setErrorCode(EnumResponseMsg.FAILED.responseCode);
            paymentDataObj.setResponseCode(EnumResponseMsg.FAILED.responseCode);
            paymentDataObj.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);
            paymentDataObj.setResponseDesc(paymentResDto.getMessage());
        }
        String result = paymentDataObj.toString();
        log.info("... Leaving doPostPaidTransactionPosting Method ...");
        System.out.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> the transaction response from : " + result);

        count = 0;
        return paymentDataObj;
    }

    public IKEDCProcessor() {
    }


    @Override
    public ElectricityProcessResponse process(ElectricityProcessRequest electricityProcessRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>the process request sent for ikeja electricity: "+ objectMapper.writeValueAsString(electricityProcessRequest));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if(electricityProcessRequest.getType().equalsIgnoreCase(Action.POSTPAID.toString())){
            return  customePayment(Action.POSTPAID.toString(), electricityProcessRequest);
        }else if(electricityProcessRequest.getType().equalsIgnoreCase(Action.PREPAID.toString())){

            return  customePayment(Action.PREPAID.toString(), electricityProcessRequest);
        }
        return null;
    }

    @Override
    public ElectricityProcessResponse reQuery(ElectricityReQueryRequest electricityReQueryRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>the requery request sent for ikeja electricity: "+ objectMapper.writeValueAsString(electricityReQueryRequest));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if(electricityReQueryRequest.getType().equalsIgnoreCase(Action.POSTPAID.toString())){
            return  requery(electricityReQueryRequest.getUniqueTransId());
        }else if(electricityReQueryRequest.getType().equalsIgnoreCase(Action.PREPAID.toString())){

            return  requery(electricityReQueryRequest.getUniqueTransId());
        }
        return null;
    }

    @Override
    public PingResponse ping(ElectricityQueryRequest electricityQueryRequest) {


            Gson gson = new Gson();
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> the ping request sent for Ikeja electricity ping is: "+gson.toJson(electricityQueryRequest));

            String disco = "IKEJA";
            String vendType = electricityQueryRequest.getType().equalsIgnoreCase("1")?"prepaid":electricityQueryRequest.getType().equalsIgnoreCase("2")?"postpai":null;
            String vertical = "ELECTRICITY";
            Boolean orderId = Boolean.valueOf(false);
            // String meterNo = electricityQueryRequest.getPayerId();

        ValidationResDto validationResDto = null;

        ValidationReqDto validationReqDto = new ValidationReqDto();
        validationReqDto.setRequestNumber(meterNo);
        validationReqDto.setType(vendType);
        String stringReq = gson.toJson(validationReqDto);

            PingResponse pingResponse = new PingResponse();

            try {
                validationResDto = accountQueryProcessor.checkMeter(meterNo, vendType, stringReq, baseUrl, verifyEndpoint, token);

//            }
                if (validationResDto != null && validationResDto.isSuccess()) {
                    pingResponse.setMessage(EnumResponseMsg.SUCCESS.responseMsg);
                    pingResponse.setCode(EnumResponseMsg.SUCCESS.responseCode);
                } else {
                    pingResponse.setMessage(EnumResponseMsg.FAILED.responseMsg);
                    pingResponse.setCode(EnumResponseMsg.FAILED.responseCode);
                }

            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                log.info("Timeout occurred during meter check: " + e);
                pingResponse.setMessage(EnumResponseMsg.TIMEOUT.responseMsg);
                pingResponse.setCode(EnumResponseMsg.TIMEOUT.responseCode);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("Exception during meter check: " + e.getMessage());
                pingResponse.setMessage(EnumResponseMsg.FAILED.responseMsg);
                pingResponse.setCode(EnumResponseMsg.FAILED.responseCode);
            }
            return pingResponse;
    }
}
