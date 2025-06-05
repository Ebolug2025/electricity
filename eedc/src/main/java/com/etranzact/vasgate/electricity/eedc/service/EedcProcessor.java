package com.etranzact.vasgate.electricity.eedc.service;

import com.etranzact.vasgate.electricity.eedc.actionenum.Action;
import com.etranzact.vasgate.electricity.eedc.actionenum.EnumResponseMsg;
import com.etranzact.vasgate.electricity.eedc.util.EEDCService;

import com.etranzact.vasgate.electricity.phcnnode.PHCNNode;
import com.etranzact.vasgate.electricity.phcnnode.dto.*;
//import com.etz.vasgate.lib.RedisUtility;
import com.etranzact.vasgate.electricity.redisutility.redisservice.NewVasgateRedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;
import java.util.Properties;
@Service
@Slf4j
@Component("phcnenu")
public class EedcProcessor extends PHCNNode {

    @Autowired
    NewVasgateRedisService redisService;

  //  private static org.apache.log4j.log log ;

//    public static String username;
//    public static String password;
//    public static String baseUrl;
//   public static String apikey;
//    public static String auth_url;
  //  public static String origin;
 private static Properties prop;
//    private final String phone;
//    private static int timeout;
//    private static int requeryDelay;
//    private static int lookupRetry;
//    private final String phoneRegex;
    //private static double maxAmount;
    @Autowired
    private final EEDCService service;

    //private static String runningMode;
  //   private Logger logger;
//    static {
//        prop = new Properties();
//        try {
           // prop.load(new FileInputStream(new File("application.properties")));
            //runningMode = prop.getProperty("RUNNING_MODE");
          //  baseUrl = prop.getProperty("ENU_BASEURL");
         //   apikey = prop.getProperty("ENU_API_KEY");
         //   origin = prop.getProperty("ENU_ORIGIN");
           // timeout = Integer.parseInt(prop.getProperty("ENU_TIMEDOUT"));
            //requeryDelay = Integer.parseInt(prop.getProperty(" "));
           // lookupRetry = Integer.parseInt(prop.getProperty("ENU_LOOKUP_RETRY"));
            //maxAmount = Double.parseDouble(prop.getProperty("ENU_MAX_AMOUNT"));

//            @Value("${ENU_BASEURL}")
//            private String baseUrl;
//
//            @Value("${ENU_API_KEY}")
//            private String apiKey;
//
//            @Value("${ENU_ORIGIN}")
//            private String origin;

            @Value("${ENU_TIMEOUT}")
            private String timeout;

            @Value("${ENU_LOOKUP_DELAY}")
            private int requeryDelay;

            @Value("${ENU_LOOKUP_RETRY}")
            private int lookupRetry;

            @Value("${ENU_DEFAULT_MOBILE}")
            private String phone;

            @Value("${PHONE_REGEX}")
            private String phoneRegex;

            @Value("${EEDC_PING_ACCOUNT}")
            private String pingaccount;



//            username = "09cc4f8d76f2_demo";
//            password = "bCG#v4*Mj0*TZb9_g7uW^";
//            baseUrl = "https://api.kvg.com.ng/";
//            hash = "0192572A2678DA5ADC5CD8B9B";
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }

            public EedcProcessor(@Value("${ENU_BASEURL}") String baseUrl,
                                 @Value("${ENU_API_KEY}") String apiKey,@Value("${ENU_ORIGIN}")String origin) {
                service = new EEDCService(apiKey, baseUrl, origin);
            }


//        phone = prop.getProperty("ENU_DEFAULT_MOBILE");
//        phoneRegex = prop.getProperty("PHONE_REGEX");
  //  }


    @Override
    public ElectricityQueryResponse query(ElectricityQueryRequest electricityQueryRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ElectricityQueryResponse response = new ElectricityQueryResponse();
        try {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>the request sent for Enugu electricity: " + objectMapper.writeValueAsString(electricityQueryRequest));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (electricityQueryRequest.getType().equalsIgnoreCase("2")) {
            response =  doPostPaidCustomerInfo(electricityQueryRequest);
        } else if (electricityQueryRequest.getType().equalsIgnoreCase("1")) {

            response = doPrepaidInfoPosting(electricityQueryRequest);
        }else{
            response.setResponseCode(EnumResponseMsg.INVALID_METER_TYPE.responseCode);
            response.setResponseDesc(EnumResponseMsg.INVALID_METER_TYPE.responseMsg);
            response.setErrorCode(EnumResponseMsg.INVALID_METER_TYPE.responseCode);
        }
        return response;
    }


    @Override
    public ElectricityProcessResponse process(ElectricityProcessRequest electricityProcessRequest) {
        ElectricityProcessResponse response = new ElectricityProcessResponse();
        if (electricityProcessRequest.getType().equalsIgnoreCase("1")) {

            response = doPrepaidTransactionPosting(electricityProcessRequest);
        } else if (electricityProcessRequest.getType().equalsIgnoreCase("2")) {

            response = doPostPaidTransactionPosting(electricityProcessRequest);
        }else{
            response.setResponseCode(EnumResponseMsg.INVALID_METER_TYPE.responseCode);
            response.setResponseDesc(EnumResponseMsg.INVALID_METER_TYPE.responseMsg);
            response.setErrorCode(EnumResponseMsg.INVALID_METER_TYPE.responseCode);
        }
        return response;
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

    @Override
    public PingResponse ping(ElectricityQueryRequest electricityQueryRequest) {

                //todo implement ping
        //String accountOrMeterNo = electricityQueryRequest.getPayerId();
        log.info(">>>>>>>>>>>>>>>>>>>>>>> the ping account number is: "+pingaccount);
        PingResponse pingResponse = new PingResponse();
        try {
            String[] postResponse = service.validateMeter(pingaccount, "Prepaid");

            if (postResponse[0].equalsIgnoreCase("200")){

                pingResponse.setMessage("success");
                pingResponse.setCode("00");
            }
        } catch (Exception e) {
           e.printStackTrace();
            pingResponse.setCode("05");
            pingResponse.setMessage("failed");

        }


        return pingResponse;
    }

    public ElectricityProcessResponse doPostPaidTransactionPosting(ElectricityProcessRequest electricityProcessRequest) {

                ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> the postpaid transaction request sent is: "+objectMapper.writeValueAsString(electricityProcessRequest));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String amount = String.valueOf(electricityProcessRequest.getAmount());
        String accountOrMeterNo = electricityProcessRequest.getPayerId();
        String uniqueTransId = electricityProcessRequest.getReference();
        ElectricityProcessResponse electricityProcessResponse = new ElectricityProcessResponse();
        MainTokenData mainTokenData = new MainTokenData();
        String name = electricityProcessRequest.getName();
        // String district = electricityProcessRequest.get get("district").getAsString().trim();

        String meterNumber;
        String account;
        String tariffCode;
        String district;

//        redis.set("phcnenu-account-" + accountOrMeterNo, accountNumber);
//                    redis.set("phcnenu-meter-" + accountNumber, meterNumber);
        long start = System.currentTimeMillis();
     //   RedisUtility redis = new RedisUtility("");

        account = (String) redisService.getValue("phcnppenu-account-" + accountOrMeterNo); //.get("phcnenu-account-" + accountOrMeterNo);
        meterNumber = (String) redisService.getValue("phcnppenu-meter-" + accountOrMeterNo); //redis.get("phcnenu-meter-" + accountOrMeterNo);
        tariffCode = (String) redisService.getValue("phcnppenu-tarrif-" + accountOrMeterNo); //redis.get("phcnenu-tarrif-" + accountOrMeterNo);
        district = (String) redisService.getValue("phcnppenu-district-" + accountOrMeterNo); //redis.get("phcnppenu-district-" + accountOrMeterNo);

        if(district == null || district.isEmpty()){
            ElectricityProcessResponse electricityProcessResponse1 = new ElectricityProcessResponse();

            electricityProcessResponse.setResponseCode(EnumResponseMsg.NOT_FOUND.responseCode);
            electricityProcessResponse.setResponseDesc("DISCTRICT NOT FOUND");
            return electricityProcessResponse;
        } else if (tariffCode == null || tariffCode.isEmpty()) {

            ElectricityProcessResponse electricityProcessResponse1 = new ElectricityProcessResponse();

            electricityProcessResponse.setResponseCode(EnumResponseMsg.NOT_FOUND.responseCode);
            electricityProcessResponse.setResponseDesc("TARIFF CODE NOT FOUND");
            return electricityProcessResponse;
        } else if (account == null || account.isEmpty()) {

            ElectricityProcessResponse electricityProcessResponse1 = new ElectricityProcessResponse();

            electricityProcessResponse.setResponseCode(EnumResponseMsg.NOT_FOUND.responseCode);
            electricityProcessResponse.setResponseDesc("ACCOUNT NUMBER NOT FOUND");
            return electricityProcessResponse;
        }


        log.info(">>>>>>>>>>>>>>>>>>>>the prepaid tarrif code from redis is: " + tariffCode);

        removeCustomerFromRedis("phcnppenu-meter-" + accountOrMeterNo, "phcnppenu-account-" + accountOrMeterNo, "phcnppenu-tarrif-" + accountOrMeterNo,"phcnppenu-district-" + accountOrMeterNo);
        //accountOrMeterNo =
        log.info("Redis Set response account - " + account);
        log.info("Redis Set response meter - " + meterNumber);
        log.info("Redis Set response - " + (System.currentTimeMillis() - start) + " ms");
        //district = district.split(" ")[0];
        //  district = district.charAt(0) + district.substring(1).toLowerCase();
        // String paymentChannel = jsonData.get("channelCode").getAsString();
        String mobileNo = electricityProcessRequest.getMobile();
        log.info("PHONE ========= " + mobileNo + " mobilE LENGTH :: " + mobileNo.length());
//        if (mobileNo.isEmpty() || mobileNo.length() < 11 || (mobileNo.startsWith("234") && mobileNo.length() != 13) || (mobileNo.startsWith("0") && mobileNo.length() != 11)) {
//            mobileNo = phone;
//            log.info("MOBILE ========= " + mobileNo);
//        }
        //String mobileNo = jsonData.get("mobileNo").getAsString();
        if (!mobileNo.isEmpty()) {
            if (mobileNo.length() == 13 || mobileNo.length() == 11) {
                if (mobileNo.length() == 13) {
                    if (mobileNo.startsWith("234")) {
                        mobileNo = "0" + mobileNo.substring(3);
                    } else {
                        mobileNo = phone;
                    }

                } else {
                    if (!mobileNo.startsWith("0")) {
                        mobileNo = phone;
                    }
                }
            }else{
                mobileNo = phone;
            }
        }
        JsonObject paymentDataObj = new JsonObject();
        String accNum = accountOrMeterNo;
//        if (accountOrMeterNo.length() != 12) {
//            accNum = accountOrMeterNo.substring(0, 2) + "/" + accountOrMeterNo.substring(2, 4) + "/" + accountOrMeterNo.substring(4, 6) + "/" + accountOrMeterNo.substring(6, 10) + "-" + accountOrMeterNo.substring(10);
//        }
        //
        //  String name = electricityProcessRequest get("customerName").getAsString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        paymentDataObj.addProperty("requestType", "payment");
        paymentDataObj.addProperty("disco", "ENU");
        paymentDataObj.addProperty("accountType", "postpaid");
        paymentDataObj.addProperty("uniqueTransId", electricityProcessRequest.getReference());
        paymentDataObj.addProperty("accountNumber", electricityProcessRequest.getPayerId());
//        double amt2 = Double.parseDouble(amount);
//        if(amt2 > maxAmount)
//        {
//            paymentDataObj.addProperty("errorCode", "");
//            paymentDataObj.addProperty("responseCode", "06");
//            paymentDataObj.addProperty("responseDesc", "Maximum amount exceeded");
//            return paymentDataObj.toString();
//        }
        JsonObject request = new JsonObject();
        request.addProperty("transactionRef", uniqueTransId);
        request.addProperty("accountNumber", account);
        request.addProperty("meterNumber", meterNumber);
        request.addProperty("customerName", name);
        request.addProperty("paymentType", "bill");
        request.addProperty("amount", Double.parseDouble(amount));
        request.addProperty("currency", "NGN");
        request.addProperty("phoneNumber", mobileNo);
        request.addProperty("paymentPlan", "Postpaid");
        request.addProperty("customerDistrict", district);
        request.addProperty("tariffCode", tariffCode);
        try {
            log.info("REQUEST ==== " + request.toString());
            String[] postResponse = service.vendPin(request, Integer.parseInt(timeout));
            log.info("RESPONSE CODE ==== " + postResponse[0]);
            if (postResponse[0].equalsIgnoreCase("200")) {
                log.info("ENU POSTPAID PAYMENT RESPONSE : " + postResponse[1]);

                JsonObject responseObj = new JsonParser().parse(postResponse[1]).getAsJsonObject();

                 //JsonObject otherToken = new JsonObject();
                //  System.out.println("Double Token :: stdToken|bsstToken :: " + tokenField);
                String respcode = responseObj.get("responseCode").getAsString();

                String responseMessage = responseObj.get("responseMessage").getAsString();
                if (!respcode.equals("200")) {
                    //paymentDataObj.addProperty("errorCode", respcode);
                    //paymentDataObj.addProperty("responseMessage", responseMessage);
                    paymentDataObj.addProperty("errorCode", respcode);
                    paymentDataObj.addProperty("responseCode", "06");
                    paymentDataObj.addProperty("responseDesc", responseMessage);
                    electricityProcessResponse.setResponseCode(EnumResponseMsg.FAILED.responseCode);
                    electricityProcessResponse.setResponseDesc(responseMessage);
                    return electricityProcessResponse;
                }
                //String tariffCode = responseObj.get("tariffCode").getAsString();
                //String firstToken = responseObj.get("token").getAsString();
                String vat = responseObj.get("vat").getAsString();
                String amt = responseObj.get("amountPaid").getAsString();
                String unit = responseObj.get("units").getAsString();
                String extRef = responseObj.get("invoiceNumber").getAsString();
                String recieptNum = extRef;
                //JsonObject custDetail = responseObj.get("CustomerDetail").getAsJsonObject();
                String arrears = responseObj.get("arrearsBalance").getAsString();

                //String customerArrears = responseObj.get("customerArrears").getAsString();
                //String secondToken = tokenField.substring(index + 1);
//                mainTokenData.setUnit(unit); //KILOWATT
//                mainTokenData.setAmount(amt); //AMOUNT
//                mainTokenData.setVat(vat); //TAX
//                mainTokenData.setFixedCharge(""); //FIXED CHARGE
                //mainTokenData.setToken(firstToken); //TOKEN
                electricityProcessResponse.setMainToken(mainTokenData);

//                otherToken.addProperty("unit", ""); //KILOWATT
//                otherToken.addProperty("amount", ""); //AMOUNT
//                otherToken.addProperty("vat", ""); //TAX
//                otherToken.addProperty("fixedCharge", ""); //FIXED CHARGEReceiptNumber
//                otherToken.addProperty("token", secondToken); //TOKEN
                electricityProcessResponse.setReceiptNo(recieptNum);
                electricityProcessResponse.setDisco("ENU");
                electricityProcessResponse.setAmount(amt);
                electricityProcessResponse.setAccountType(Action.POSTPAID.toString());
                electricityProcessResponse.setRequestType("Payment");
                electricityProcessResponse.setUniqueTransId(uniqueTransId);
                electricityProcessResponse.setExternalReference(recieptNum);
                paymentDataObj.addProperty("customerArrears", arrears);
                electricityProcessResponse.setCustomerArrears(arrears);
                //  paymentDataObj.addProperty("undertaking", tariffCode);

                paymentDataObj.addProperty("undertaking", "");

                //  paymentDataObj.addProperty("customerName", name);
                //   paymentDataObj.addProperty("businessUnit", district);
                electricityProcessResponse.setBusinessUnit(district);
                electricityProcessResponse.setErrorCode(respcode);

                electricityProcessResponse.setExternalReference(extRef);
                electricityProcessResponse.setResponseCode(EnumResponseMsg.SUCCESS.responseCode);
                electricityProcessResponse.setResponseDesc(EnumResponseMsg.SUCCESS.responseMsg);
                electricityProcessResponse.setErrorCode(EnumResponseMsg.SUCCESS.responseCode);

            } else {

                electricityProcessResponse.setResponseCode(EnumResponseMsg.FAILED.responseCode);
                electricityProcessResponse.setResponseDesc(EnumResponseMsg.SUCCESS.responseMsg);
                electricityProcessResponse.setErrorCode(postResponse[0]);

            }

        } catch (SocketTimeoutException ex) {
            int count = 0;
            while (count < lookupRetry) {
                try {
                    log.info(uniqueTransId + " :: Sleeping for " + requeryDelay + " secs (" + (count + 1) + ")");
                    Thread.sleep(requeryDelay * 1000);//030000000400003400081
                    String[] postResponse = service.requery(uniqueTransId);

                    if (postResponse[0].trim().equals("200")) {

                        JsonObject responseObj = new JsonParser().parse(postResponse[1]).getAsJsonObject();
                        if (responseObj.get("responseCode").getAsString().equals("200")) {
                            JsonObject mainToken = new JsonObject();
                            //JsonObject otherToken = new JsonObject();

                            //  System.out.println("Double Token :: stdToken|bsstToken :: " + tokenField);
                            String firstToken = "";
                            try {
                                firstToken = responseObj.get("token").getAsString();
                            } catch (Exception exp) {
                                firstToken = "";
                            }
                            String vat = "";
                            try {
                                vat = responseObj.get("vat").getAsString();
                            } catch (Exception exp) {
                                firstToken = "";
                            }
                            String extRef = "";
                            try {
                                extRef = responseObj.get("transactionRef").getAsString();
                            } catch (Exception exp) {
                                extRef = "";
                            }
                            //String vat = responseObj.get("vat").getAsString();
                            String amt = responseObj.get("amountPaid").getAsString();
                            String unit = responseObj.get("units").getAsString();
                            String respcode = responseObj.get("responseCode").getAsString();
                            String ref = responseObj.get("transactionRef").getAsString();

                            //String secondToken = tokenField.substring(index + 1);
                            log.info("RESPONSE CODE REQUERY :: == =============" + postResponse[0]);
                            mainTokenData.setUnit(unit); //KILOWATT
                            mainTokenData.setAmount(amt); //AMOUNT
                            mainTokenData.setVat(vat); //TAX
                            mainTokenData.setFixedCharge(""); //FIXED CHARGE
                            mainTokenData.setToken(firstToken); //TOKEN
                            electricityProcessResponse.setErrorCode(respcode);
                            electricityProcessResponse.setUniqueTransId(ref);
                            electricityProcessResponse.setExternalReference(extRef);
                            electricityProcessResponse.setMainToken( mainTokenData);
                            electricityProcessResponse.setResponseCode(EnumResponseMsg.SUCCESS.responseCode);
                            electricityProcessResponse.setResponseDesc(EnumResponseMsg.SUCCESS.responseMsg);

                            break;
                        } else {
                            count++;
                            //log.error("PaymentPost Failure");
                            electricityProcessResponse.setResponseCode(EnumResponseMsg.FAILED.responseCode);
                            electricityProcessResponse.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);
                            electricityProcessResponse.setErrorCode(responseObj.get("responseCode").getAsString());

                        }
                    } else {
                        count++;
                        // log.error("PaymentPost Failure", ex);
                        electricityProcessResponse.setResponseCode(EnumResponseMsg.FAILED.responseCode);
                        electricityProcessResponse.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);
                        electricityProcessResponse.setErrorCode(postResponse[0].trim());
                    }
                } catch (Exception e) {
                    count++;
                    log.error("PaymentPost Failure", ex);
                    electricityProcessResponse.setResponseCode(EnumResponseMsg.FAILED.responseCode);
                    electricityProcessResponse.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);
                    electricityProcessResponse.setErrorCode(EnumResponseMsg.FAILED.responseCode);
                }

            }
            return electricityProcessResponse;

        } catch (Exception ex) {
            log.error("Transaction Posting", ex);
            electricityProcessResponse.setResponseCode(EnumResponseMsg.FAILED.responseCode);
            electricityProcessResponse.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);
            electricityProcessResponse.setErrorCode(EnumResponseMsg.FAILED.responseCode);

            try {
                log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>> the final response from enugu vending service is: "+objectMapper.writeValueAsString(electricityProcessResponse));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>> the final response from enugu vending service is: "+objectMapper.writeValueAsString(electricityProcessResponse));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return electricityProcessResponse;

    }


    public ElectricityQueryResponse doPrepaidInfoPosting(ElectricityQueryRequest electricityQueryRequest) {

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String result;
        String accountOrMeterNo = electricityQueryRequest.getPayerId();
        String uniqueTransId = electricityQueryRequest.getReference();
        JsonObject verificationDataObj = new JsonObject();
        ElectricityQueryResponse response = new ElectricityQueryResponse();
        verificationDataObj.addProperty("requestType", "verification");
        response.setRequestType("verification");
        response.setDisco("ENU");
        response.setUniqueTransId(uniqueTransId);
        response.setAccountNumber(accountOrMeterNo);
        verificationDataObj.addProperty("disco", "ENU");
        verificationDataObj.addProperty("accountType", "Prepaid");
        verificationDataObj.addProperty("uniqueTransId", uniqueTransId);
        verificationDataObj.addProperty("accountNumber", accountOrMeterNo);

        if (accountOrMeterNo.length() == 12) {
            accountOrMeterNo = accountOrMeterNo.substring(0, 2) + "/" + accountOrMeterNo.substring(2, 4) + "/" + accountOrMeterNo.substring(4, 6) + "/" + accountOrMeterNo.substring(6, 10) + "-" + accountOrMeterNo.substring(10);

        }

//        if (accountOrMeterNo.length() != 12) {
//            verificationDataObj.addProperty("errorCode", "");
//            verificationDataObj.addProperty("responseCode", "06");
//            verificationDataObj.addProperty("responseDesc", "Invalid Account Number");
//            return verificationDataObj.toString();
//        }
        //String accNum = accountOrMeterNo.substring(0, 2) + "/" + accountOrMeterNo.substring(2, 4) + "/" + accountOrMeterNo.substring(4, 6) + "/" + accountOrMeterNo.substring(6, 10) + "-" + accountOrMeterNo.substring(10);
        try {
            log.info("CALLING ENUGU");
            //log.info("ACCOUNT :: " + accNum);
            String[] postResponse = service.validateMeter(accountOrMeterNo, "Prepaid");
            log.info("RESPONSE CODE ==== " + postResponse[0]);
            if (postResponse[0].equalsIgnoreCase("200")) { //Not Timeout

                log.info("ENU PREPAID VERIFICATION RESPONSE : " + postResponse[1]);
                JsonObject responseObj = new JsonParser().parse(postResponse[1]).getAsJsonObject();
                log.info("ENU RESPONSE : " + responseObj);
                int code = responseObj.get("responseCode").getAsInt();
                String message = responseObj.get("responseMessage").getAsString();
                log.info("ENU RESPONSE CODE : " + code);
                if (code == 200) {
                    JsonObject customerDetal = responseObj.getAsJsonObject("customer");
                    //JsonObject meterDetail = responseObj.getAsJsonObject("MeterDetail");
                    String firstname;

                    try {
                        firstname = customerDetal.get("firstName").getAsString();
                        log.info("LAST NAME :: " + firstname);
                    } catch (Exception e) {
                        firstname = "";
                    }
                    String lastname;
                    try {
                        lastname = customerDetal.get("lastName").getAsString();
                        log.info("LAST NAME :: " + lastname);
                    } catch (Exception e) {
                        lastname = "";
                    }
                    String meterNumber = customerDetal.get("meterNumber").getAsString();
                    String accountNumber = customerDetal.get("accountNumber").getAsString();
                    String tariffCode = customerDetal.get("tariffCode").getAsString();
                    String district = customerDetal.get("district").getAsString();

                    if(district != null && !district.isEmpty()) {
                        district = district.split(" ").length == 1 ? district+" business unit":district;
                    }else{
                        district = "ENU BUSINESS UNIT";
                    }
                    long start = System.currentTimeMillis();
               //     RedisUtility redis = new RedisUtility("");
                    redisService.setValue("phcnenu-account-" + accountOrMeterNo, accountNumber);
                    redisService.setValue("phcnenu-meter-" + accountOrMeterNo, meterNumber);
                    redisService.setValue("phcnenu-tarrif-" + accountOrMeterNo, tariffCode);
                    redisService.setValue("phcnenu-district-"+accountOrMeterNo,district);
                    log.info(">>>>>>>>>>>>>>>>>>>>the prepaid tarrif code added to redis is: " + tariffCode);


                    log.info("Redis Set response - " + (System.currentTimeMillis() - start) + " ms");
                    String name = firstname + " " + lastname;
                    verificationDataObj.addProperty("meterNumber", meterNumber);
                    verificationDataObj.addProperty("customerName", name);
                    verificationDataObj.addProperty("tariffCode", tariffCode);
                    response.setCustomerName(name);
                    response.setTariff(tariffCode);

                    String customerAddress;
                    try {
                        customerAddress = customerDetal.get("address").getAsString();
                    } catch (Exception e) {
                        customerAddress = "";
                    }
                   // String district;
                    try {
                        district = customerDetal.get("district").getAsString();
                    } catch (Exception e) {
                        district = "";
                    }
                    //verificationDataObj.addProperty("customerAddress", customerAddress);
                    //verificationDataObj.addProperty("businessUnit", district+" BUSINESS UNIT");
                    verificationDataObj.addProperty("customerAddress", customerAddress);
                    verificationDataObj.addProperty("businessUnit", district + " BUSINESS UNIT");

                    response.setBusinessUnit(district + " BUSINESS UNIT");
                    response.setCustomerAddress(customerAddress);

                    String state;
                    try {
                        state = customerDetal.get("state").getAsString();
                    } catch (Exception e) {
                        state = "";
                    }
                    verificationDataObj.addProperty("state", state);
                    verificationDataObj.addProperty("minimumPurchase", "0");
                    verificationDataObj.addProperty("customerArrears", customerDetal.get("arrearsBalance").getAsString());
                    verificationDataObj.addProperty("externalReference", "");
//                    verificationDataObj.addProperty("phone", "");
                    verificationDataObj.addProperty("errorCode", code);
                    verificationDataObj.addProperty("responseCode", "00");
                    verificationDataObj.addProperty("responseDesc", message);
                    verificationDataObj.addProperty("email", "");
                    verificationDataObj.addProperty("tariffRate", customerDetal.get("tariffRate").getAsString());
                    verificationDataObj.addProperty("phoneNumber", "");
                    verificationDataObj.addProperty("tariff", customerDetal.get("tariffRate").getAsString());
                    verificationDataObj.addProperty("tariffCode", customerDetal.get("tariffCode").getAsString());
                    verificationDataObj.addProperty("customerType", "");

                    response.setState(state);
                    response.setMinimumPurchase("0");
                    response.setCustomerArrears(customerDetal.get("arrearsBalance").getAsString());
                    response.setExternalReference("");
                    response.setErrorCode(String.valueOf(code));
                    response.setResponseCode("00");
                    response.setResponseDesc(message);
                    response.setTariff(customerDetal.get("tariffRate").getAsString());

                    //verificationDataObj.addProperty("minVendAmount", responseObj.get("MinVendAmount").getAsString());
                    //verificationDataObj.addProperty("maxVendAmount", responseObj.get("MaxVendAmount").toString());
                } else {
                    log.info("ENU RESPONSE : " + responseObj);
                    verificationDataObj.addProperty("errorCode", code);
                    verificationDataObj.addProperty("responseCode", "56");
                    verificationDataObj.addProperty("responseDesc", code + " - " + message);

                    response.setErrorCode(String.valueOf(code));
                    response.setResponseDesc(code + " - " + message);
                    response.setResponseCode("56");
                }

            } else {
                verificationDataObj.addProperty("responseCode", "06");
                verificationDataObj.addProperty("responseDesc", "Request Timeout");
                response.setResponseDesc("Account number details not found");
                response.setResponseCode("06");
            }

        } catch (Exception e) {
            log.error("Error : ", e);
            verificationDataObj.addProperty("responseCode", "56");
            verificationDataObj.addProperty("responseDesc", "Account number details not found");
            response.setResponseDesc("Account number details not found");
            response.setResponseCode("56");
        }

        result = verificationDataObj.toString();
        return response;
    }


    public ElectricityQueryResponse doPostPaidCustomerInfo(ElectricityQueryRequest electricityQueryRequest) {
        String result;
        String accNum;
        String accountOrMeterNo = electricityQueryRequest.getPayerId();
        String uniqueTransId = electricityQueryRequest.getReference();
        JsonObject verificationDataObj = new JsonObject();
        ElectricityQueryResponse response = new ElectricityQueryResponse();
        log.info("ACCOUNT BEFORE FORMAT :: " + accountOrMeterNo);
        accNum = accountOrMeterNo;
        if (accountOrMeterNo.length() == 12) {
            accNum = accountOrMeterNo.substring(0, 2) + "/" + accountOrMeterNo.substring(2, 4) + "/" + accountOrMeterNo.substring(4, 6) + "/" + accountOrMeterNo.substring(6, 10) + "-" + accountOrMeterNo.substring(10);
        }

        verificationDataObj.addProperty("requestType", "verification");
        verificationDataObj.addProperty("disco", "ENU");
        verificationDataObj.addProperty("accountType", "Postpaid");
        verificationDataObj.addProperty("uniqueTransId", uniqueTransId);
        verificationDataObj.addProperty("accountNumber", accNum);

        try {
            log.info("CALLING ENUGU");
            log.info("ACCOUNT :: " + accNum);
            String[] postResponse = service.validateMeter(accNum, "Postpaid");
            log.info("RESPONSE CODE ==== " + postResponse[0]);
            if (postResponse[0].equalsIgnoreCase("200")) { //Not Timeout

               // RedisUtility redis = new RedisUtility("");


                //log.info("ENU POSTPAID VERIFICATION RESPONSE : " + postResponse[1]);
                JsonObject responseObj = new JsonParser().parse(postResponse[1]).getAsJsonObject();
                int code = responseObj.get("responseCode").getAsInt();
                String message = responseObj.get("responseMessage").getAsString();
                if (code == 200) {
                    JsonObject customerDetal = responseObj.getAsJsonObject("customer");
                    //JsonObject meterDetail = responseObj.getAsJsonObject("MeterDetail");
                    String firstname;

                    try {
                        firstname = customerDetal.get("firstName").getAsString();
                        log.info("FIRST NAME :: " + firstname);
                    } catch (Exception e) {
                        firstname = "";
                    }
                    String lastname;
                    try {
                        lastname = customerDetal.get("lastName").getAsString();
                        log.info("LAST NAME :: " + lastname);
                    } catch (Exception e) {
                        lastname = "";
                    }
                    String name = firstname + " " + lastname;
                    response.setCustomerName(name);
                    response.setRequestType("Verification");
                    response.setUniqueTransId(uniqueTransId);
                    response.setDisco("ENU");
                    response.setCustomerType("POSTPAID");

                    verificationDataObj.addProperty("meterNumber", customerDetal.get("meterNumber").getAsString());
                    verificationDataObj.addProperty("customerName", name);
                    String accountNumber = "";
                    try{
                        accountNumber  =  customerDetal.get("accountNumber").getAsString();

                    }catch (Exception e){
                        accountNumber = "";
                    }
                    response.setAccountNumber(accountNumber);
                    String customerAddress;
                    try {
                        customerAddress = customerDetal.get("address").getAsString();
                        response.setCustomerAddress(customerAddress);
                    } catch (Exception e) {
                        customerAddress = "";
                    }
                    String district;
                    try {
                        district = customerDetal.get("district").getAsString();

                        if(district != null && !district.isEmpty()) {
                            district = district.split(" ").length == 1 ? district+" business unit":district;
                        }else{
                            district = "ENU BUSINESS UNIT";
                        }

                    } catch (Exception e) {
                        district = "JOS BUSINESS UNIT";

                    }
                    verificationDataObj.addProperty("customerAddress", customerAddress);
                    verificationDataObj.addProperty("businessUnit", district + " BUSINESS UNIT");
                    response.setBusinessUnit(district + " BUSINESS UNIT");
                    String state;
                    try {
                        state = customerDetal.get("state").getAsString();

                    } catch (Exception e) {
                        state = "";
                    }
                    try {
                        redisService.setValue("phcnppenu-tarrif-" + accountOrMeterNo, customerDetal.get("tariffCode").getAsString());
                        redisService.setValue("phcnppenu-meter-" + accountOrMeterNo, accountOrMeterNo);
                        redisService.setValue("phcnppenu-account-" + accountOrMeterNo, accountNumber);
                        redisService.setValue("phcnppenu-district-" + accountOrMeterNo, district);
                        log.info(">>>>>>>>>>>>>>>>>>>>the tarrif code added to redis is: " + customerDetal.get("tariffCode").getAsString());
                    } catch (Exception e) {
                        log.info(">>>>>>>>>>>>>>>>>>>>>>>>redis error:");
                        log.error("redis", e);
                    }
                   // verificationDataObj.addProperty("state", state);
                    response.setState(state);
                  //  verificationDataObj.addProperty("minimumPurchase", "0");
                    response.setMinimumPurchase("0");
                  //  verificationDataObj.addProperty("customerArrears", customerDetal.get("arrearsBalance").getAsString());
                    response.setCustomerArrears(customerDetal.get("arrearsBalance").getAsString());
                 //   verificationDataObj.addProperty("externalReference", "");
                    response.setExternalReference("");
//                    verificationDataObj.addProperty("phone", "");
                 //   verificationDataObj.addProperty("errorCode", code);
                    response.setErrorCode("00");
                 //   verificationDataObj.addProperty("responseCode", "00");
                    response.setResponseCode("00");
                //    verificationDataObj.addProperty("responseDesc", message);
                    response.setResponseDesc(message);
                  //  verificationDataObj.addProperty("email", "");
                    response.setEmail("");
                    verificationDataObj.addProperty("tariffRate", customerDetal.get("tariffRate").getAsString());
                    verificationDataObj.addProperty("phoneNumber", "");
                   // verificationDataObj.addProperty("tariff", customerDetal.get("tariffRate").getAsString());
                    response.setTariff(customerDetal.get("tariffRate").getAsString());
                    //verificationDataObj.addProperty("tariffIndex", meterDetail.get("TariffIndex").getAsString());
                   // verificationDataObj.addProperty("tariffCode", customerDetal.get("tariffCode").getAsString());
                    response.setTariffDesc(customerDetal.get("tariffCode").getAsString());
                    verificationDataObj.addProperty("customerType", "");

                    //verificationDataObj.addProperty("minVendAmount", responseObj.get("MinVendAmount").getAsString());
                    //verificationDataObj.addProperty("maxVendAmount", responseObj.get("MaxVendAmount").toString());
                } else {
                   // verificationDataObj.addProperty("errorCode", code);
                    response.setErrorCode(String.valueOf(code));
                    //verificationDataObj.addProperty("responseCode", "56");
                    response.setResponseCode("56");
                   // verificationDataObj.addProperty("responseDesc", code + " - " + message);
                    response.setResponseDesc(code + " - " + message);
                }

            } else {
                response.setResponseCode("06");
                response.setResponseDesc("Request Timeout");
                verificationDataObj.addProperty("responseCode", "06");
                verificationDataObj.addProperty("responseDesc", "Request Timeout");
            }

        } catch (Exception e) {
            log.error("Error : ", e);
            response.setResponseCode("56");
            response.setResponseDesc("Account number details not found");
            verificationDataObj.addProperty("responseCode", "56");
            verificationDataObj.addProperty("responseDesc", "Account number details not found");
        }

        result = verificationDataObj.toString();

        return response;

    }

    public ElectricityProcessResponse doPrepaidTransactionPosting(ElectricityProcessRequest electricityProcessRequest) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> the prepaid transaction sent is: "+objectMapper.writeValueAsString(electricityProcessRequest));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String amount = String.valueOf(electricityProcessRequest.getAmount());
        String accountOrMeterNo = electricityProcessRequest.getPayerId();
        String uniqueTransId = electricityProcessRequest.getReference();
        ElectricityProcessResponse electricityProcessResponse = new ElectricityProcessResponse();
        MainTokenData mainTokenData = new MainTokenData();
        //  String district = electricityProcessRequest.get("district").getAsString().trim();
        String meterNumber;
        String account;
        String tariffCode;
        String district;

//        redis.set("phcnenu-account-" + accountOrMeterNo, accountNumber);
//                    redis.set("phcnenu-meter-" + accountNumber, meterNumber);
        long start = System.currentTimeMillis();
     //   RedisUtility redis = new RedisUtility("");

        account = (String) redisService.getValue("phcnenu-account-" + accountOrMeterNo); //redis.get("phcnenu-account-" + accountOrMeterNo);
        meterNumber = (String) redisService.getValue("phcnenu-meter-" + accountOrMeterNo); //redis.get("phcnenu-meter-" + accountOrMeterNo);
        tariffCode = (String) redisService.getValue("phcnenu-tarrif-" + accountOrMeterNo); //redis.get("phcnenu-tarrif-" + accountOrMeterNo);
        district = (String) redisService.getValue("phcnenu-district-"+accountOrMeterNo);

        if(district == null || district.isEmpty()){
            ElectricityProcessResponse electricityProcessResponse1 = new ElectricityProcessResponse();

            electricityProcessResponse.setResponseCode(EnumResponseMsg.NOT_FOUND.responseCode);
            electricityProcessResponse.setResponseDesc("DISCTRICT NOT FOUND");
            return electricityProcessResponse;
        } else if (tariffCode == null || tariffCode.isEmpty()) {

            ElectricityProcessResponse electricityProcessResponse1 = new ElectricityProcessResponse();

            electricityProcessResponse.setResponseCode(EnumResponseMsg.NOT_FOUND.responseCode);
            electricityProcessResponse.setResponseDesc("TARIFF CODE NOT FOUND");
            return electricityProcessResponse;
        } else if (account == null || account.isEmpty()) {

            ElectricityProcessResponse electricityProcessResponse1 = new ElectricityProcessResponse();

            electricityProcessResponse.setResponseCode(EnumResponseMsg.NOT_FOUND.responseCode);
            electricityProcessResponse.setResponseDesc("ACCOUNT NUMBER NOT FOUND");
            return electricityProcessResponse;
        }

        log.info(">>>>>>>>>>>>>>>>>>>>the prepaid tarrif code from redis is: " + tariffCode);
        removeCustomerFromRedis("phcnenu-meter-" + accountOrMeterNo, "phcnenu-account-" + accountOrMeterNo, "phcnenu-tarrif-" + accountOrMeterNo,"phcnenu-district-"+accountOrMeterNo);

        //accountOrMeterNo =
        log.info("Redis Set response account - " + account);
        log.info("Redis Set response meter - " + meterNumber);
        log.info("Redis Set response - " + (System.currentTimeMillis() - start) + " ms");
        //district = district.split(" ")[0];
        //district = district.charAt(0) + district.substring(1).toLowerCase();
        // String paymentChannel = jsonData.get("channelCode").getAsString();
        String mobileNo = electricityProcessRequest.getMobile();
        log.info("PHONE ========= " + mobileNo + " mobilE LENGTH :: " + mobileNo.length());
//        if (mobileNo.isEmpty() || mobileNo.length() < 11 || (mobileNo.startsWith("234") && mobileNo.length() != 13) || (mobileNo.startsWith("0") && mobileNo.length() != 11)) {
//            mobileNo = phone;
//            log.info("MOBILE ========= " + mobileNo);
//        }
        //String mobileNo = jsonData.get("mobileNo").getAsString();
//        if (mobileNo.isEmpty() ) {
//            mobileNo = phone;
//        } else if (mobileNo.startsWith("234")) {
//            mobileNo = mobileNo.replace("234", "0");
//        }
        if (!mobileNo.isEmpty()) {
            if (mobileNo.length() == 13 || mobileNo.length() == 11) {
                if (mobileNo.length() == 13) {
                    if (mobileNo.startsWith("234")) {
                        mobileNo = "0" + mobileNo.substring(3);
                    } else {
                        mobileNo = phone;
                    }

                } else {
                    if (!mobileNo.startsWith("0")) {
                        mobileNo = phone;
                    }
                }
            }else{
                mobileNo = phone;
            }
        }
        JsonObject paymentDataObj = new JsonObject();
        String accNum = accountOrMeterNo;
//        if (accountOrMeterNo.length() != 12) {
//            accNum = accountOrMeterNo.substring(0, 2) + "/" + accountOrMeterNo.substring(2, 4) + "/" + accountOrMeterNo.substring(4, 6) + "/" + accountOrMeterNo.substring(6, 10) + "-" + accountOrMeterNo.substring(10);
//        }
        //
        String name = electricityProcessRequest.getReference();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        paymentDataObj.addProperty("requestType", "payment");
        paymentDataObj.addProperty("disco", "ENU");
        paymentDataObj.addProperty("accountType", "prepaid");
        paymentDataObj.addProperty("uniqueTransId", electricityProcessRequest.getReference());
        paymentDataObj.addProperty("accountNumber", electricityProcessRequest.getPayerId());
//        double amt2 = Double.parseDouble(amount);
//        if(amt2 > maxAmount)
//        {
//            paymentDataObj.addProperty("errorCode", "");
//            paymentDataObj.addProperty("responseCode", "06");
//            paymentDataObj.addProperty("responseDesc", "Maximum amount exceeded");
//            return paymentDataObj.toString();
//        }
        JsonObject request = new JsonObject();
        request.addProperty("transactionRef", uniqueTransId);
        request.addProperty("accountNumber", account);
        request.addProperty("meterNumber", meterNumber);
        request.addProperty("customerName", name);
        request.addProperty("paymentType", "bill");
        request.addProperty("amount", Double.parseDouble(amount));
        request.addProperty("currency", "NGN");
        request.addProperty("phoneNumber", mobileNo);
        request.addProperty("paymentPlan", "Prepaid");
        request.addProperty("customerDistrict", district);
        request.addProperty("tariffCode", tariffCode);

//        if(tariffCode == null){
//            paymentDataObj.addProperty("errorCode", EnumResponseMsg.ACCESS_CODE_NOT_FOUND);
//            paymentDataObj.addProperty("responseCode", "56");
//            paymentDataObj.addProperty("responseDesc", "Timed out");
//        }
        try {
            log.info("REQUEST ==== " + request.toString());
            String[] postResponse = service.vendPin(request, Integer.parseInt(timeout));
            log.info("RESPONSE CODE ==== " + postResponse[0]);
            if (postResponse[0].equalsIgnoreCase("200")) {
                log.info("ENU PREPAID PAYMENT RESPONSE : " + postResponse[1]);

                JsonObject responseObj = new JsonParser().parse(postResponse[1]).getAsJsonObject();

                JsonObject mainToken = new JsonObject();
                //JsonObject otherToken = new JsonObject();
                //  System.out.println("Double Token :: stdToken|bsstToken :: " + tokenField);
                String respcode = responseObj.get("responseCode").getAsString();

                String responseMessage = responseObj.get("responseMessage").getAsString();
                if (!respcode.equals("200")) {
                    //paymentDataObj.addProperty("errorCode", respcode);
                    //paymentDataObj.addProperty("responseMessage", responseMessage);

                    electricityProcessResponse.setResponseDesc(responseMessage);
                    electricityProcessResponse.setResponseCode("06");
                    electricityProcessResponse.setErrorCode(respcode);
                    return electricityProcessResponse;
                }
                //String tariffCode = responseObj.get("tariffCode").getAsString();
                String firstToken = responseObj.get("token").getAsString();
                String vat = responseObj.get("vat").getAsString();
                String amt = responseObj.get("amountPaid").getAsString();
                String unit = responseObj.get("units").getAsString();
                String extRef = responseObj.get("transactionId").getAsString();
                String recieptNum = extRef;
                //JsonObject custDetail = responseObj.get("CustomerDetail").getAsJsonObject();
                String arrears = responseObj.get("appliedToArrears").getAsString();

                //String customerArrears = responseObj.get("customerArrears").getAsString();
                //String secondToken = tokenField.substring(index + 1);
                mainTokenData.setUnit( unit); //KILOWATT
                mainTokenData.setAmount(amt); //AMOUNT
                mainTokenData.setVat(vat); //TAX
                mainTokenData.setFixedCharge(""); //FIXED CHARGE
                mainTokenData.setToken(firstToken); //TOKEN
                electricityProcessResponse.setMainToken(mainTokenData);
                electricityProcessResponse.setErrorCode(respcode);
                electricityProcessResponse.setRequestType("Payment");
                electricityProcessResponse.setDisco("ENU");
                electricityProcessResponse.setAccountType(Action.PREPAID.toString());
                electricityProcessResponse.setAmount(amt);

                electricityProcessResponse.setUniqueTransId(electricityProcessRequest.getReference());
                electricityProcessResponse.setExternalReference(extRef);
                electricityProcessResponse.setResponseCode(EnumResponseMsg.SUCCESS.responseCode);
                electricityProcessResponse.setResponseDesc(EnumResponseMsg.SUCCESS.responseMsg);

                electricityProcessResponse.setReceiptNo(recieptNum);
                electricityProcessResponse.setCustomerArrears(arrears);
                electricityProcessResponse.setTariffDescription(tariffCode);

                electricityProcessResponse.setExternalReference(extRef);

            } else {


                electricityProcessResponse.setResponseCode(EnumResponseMsg.FAILED.responseCode);
                electricityProcessResponse.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);
                electricityProcessResponse.setErrorCode(postResponse[0]);
            }

        } catch (SocketTimeoutException ex) {
            int count = 0;
            while (count < lookupRetry) {
                try {
                    log.info(uniqueTransId + " :: Sleeping for " + requeryDelay + " secs (" + (count + 1) + ")");
                    Thread.sleep(requeryDelay * 1000);//030000000400003400081
                    String[] postResponse = service.requery(uniqueTransId);

                    if (postResponse[0].trim().equals("200")) {

                        JsonObject responseObj = new JsonParser().parse(postResponse[1]).getAsJsonObject();
                        if (responseObj.get("responseCode").getAsString().equals("200")) {

                            //JsonObject otherToken = new JsonObject();

                            //  System.out.println("Double Token :: stdToken|bsstToken :: " + tokenField);
                            String firstToken = "";
                            try {
                                firstToken = responseObj.get("token").getAsString();
                            } catch (Exception exp) {
                                firstToken = "";
                            }
                            String vat = "";
                            try {
                                vat = responseObj.get("vat").getAsString();
                            } catch (Exception exp) {
                                firstToken = "";
                            }
                            String extRef = "";
                            try {
                                extRef = responseObj.get("transactionRef").getAsString();
                            } catch (Exception exp) {
                                extRef = "";
                            }
                            //String vat = responseObj.get("vat").getAsString();
                            String amt = responseObj.get("amountPaid").getAsString();
                            electricityProcessResponse.setAmount(amount);
                            String unit = responseObj.get("units").getAsString();
                            electricityProcessResponse.setUnitsPayment(unit);
                            String respcode = responseObj.get("responseCode").getAsString();
                            electricityProcessResponse.setResponseCode(respcode);
                            String ref = responseObj.get("transactionRef").getAsString();

                            //String secondToken = tokenField.substring(index + 1);
                            log.info("RESPONSE CODE REQUERY :: == =============" + postResponse[0]);
                            mainTokenData.setUnit( unit); //KILOWATT
                            mainTokenData.setAmount(amt); //AMOUNT
                            mainTokenData.setVat(vat); //TAX
                            mainTokenData.setFixedCharge(""); //FIXED CHARGE
                            mainTokenData.setToken(firstToken); //TOKEN
                            electricityProcessResponse.setMainToken(mainTokenData);
                            electricityProcessResponse.setErrorCode(respcode);
                            electricityProcessResponse.setRequestType(Action.PREPAID.toString());
                            electricityProcessResponse.setUniqueTransId(ref);
                            electricityProcessResponse.setExternalReference(extRef);
                            electricityProcessResponse.setResponseCode(EnumResponseMsg.SUCCESS.responseCode);
                            electricityProcessResponse.setResponseDesc(EnumResponseMsg.SUCCESS.responseMsg);

                        } else {
                            count++;
                            //log.error("PaymentPost Failure");
                            electricityProcessResponse.setResponseCode(EnumResponseMsg.FAILED.responseCode);
                            electricityProcessResponse.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);
                            electricityProcessResponse.setErrorCode(responseObj.get("responseCode").getAsString());

                        }
                    } else {
                        count++;
                        // log.error("PaymentPost Failure", ex);
                        electricityProcessResponse.setResponseCode(EnumResponseMsg.FAILED.responseCode);
                        electricityProcessResponse.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);
                        electricityProcessResponse.setErrorCode(postResponse[0].trim());
                    }
                } catch (Exception e) {
                    count++;
                    log.error("PaymentPost Failure", ex);
                    electricityProcessResponse.setResponseCode(EnumResponseMsg.FAILED.responseCode);
                    electricityProcessResponse.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);
                    electricityProcessResponse.setErrorCode(EnumResponseMsg.FAILED.responseCode);
                }

            }


        } catch (Exception ex) {
            log.error("Transaction Posting", ex);
            electricityProcessResponse.setResponseCode(EnumResponseMsg.FAILED.responseCode);
            electricityProcessResponse.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);
            electricityProcessResponse.setErrorCode(EnumResponseMsg.FAILED.responseCode);
        }

        try {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> the eedc prepaid payment response sent to back to vasgate-electricity is: "+objectMapper.writeValueAsString(electricityProcessResponse));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return electricityProcessResponse;


    }

//    public ElectricityProcessResponse doPostPaidTransactionReversal(ElectricityReProcessRequest electricityReProcessRequest) {
//        String uniqueTransId = electricityReProcessRequest.getReference();
//        String accountOrMeterNo = electricityReProcessRequest.getAccount();
//        JsonObject reversalDataObj = new JsonObject();
//        reversalDataObj.addProperty("requestType", "reversal");
//        reversalDataObj.addProperty("disco", "ENU");
//        reversalDataObj.addProperty("accountType", "Postpaid");
//        reversalDataObj.addProperty("uniqueTransId", electricityReProcessRequest.getReference());
//        reversalDataObj.addProperty("accountNumber", electricityReProcessRequest.getAccount());
//
//        log.info("REVERSAL REQUEST ==== " + uniqueTransId);
//        String[] postResponse;
//        try {
//            postResponse = service.reversal(uniqueTransId);
//            log.info("RESPONSE CODE ==== " + postResponse[0]);
//
//            if (postResponse[0].equalsIgnoreCase("200")) {
//                JsonObject responseObj = new JsonParser().parse(postResponse[1]).getAsJsonObject();
//                String respcode = responseObj.get("responseCode").getAsString();
//                String responseMessage = responseObj.get("responseMessage").getAsString();
//                String receipt = responseObj.get("transactionId").getAsString();
//                //String reference = responseObj.get("transactionRef").getAsString();
//                reversalDataObj.addProperty("uniqueTransId", uniqueTransId);
//                reversalDataObj.addProperty("accountNumber", accountOrMeterNo);
//                reversalDataObj.addProperty("externalReference", receipt);
//                reversalDataObj.addProperty("errorCode", respcode);
//                reversalDataObj.addProperty("responseCode", "00");
//                reversalDataObj.addProperty("responseDesc", responseMessage);
//            } else {
//                reversalDataObj.addProperty("errorCode", postResponse[0]);
//                reversalDataObj.addProperty("responseCode", "06");
//                reversalDataObj.addProperty("responseDesc", "");
//            }
//        } catch (Exception ex) {
//            reversalDataObj.addProperty("errorCode", "");
//            reversalDataObj.addProperty("responseCode", "56");
//            reversalDataObj.addProperty("responseDesc", "Timed out");
//        }
//
//        return new ElectricityProcessResponse();
//    }

    public ElectricityProcessResponse doPostPaidTransactionRequery(ElectricityReQueryRequest electricityReProcessRequest) {
        String accountOrMeterNo = electricityReProcessRequest.getPayerId();
        JsonObject requeryDataObj = new JsonObject();
        requeryDataObj.addProperty("requestType", "Requery");
        requeryDataObj.addProperty("disco", "ENU");
        requeryDataObj.addProperty("accountType", "Postpaid");
        requeryDataObj.addProperty("uniqueTransId", electricityReProcessRequest.getReference());
        requeryDataObj.addProperty("accountNumber", electricityReProcessRequest.getPayerId());
        String accNum = accountOrMeterNo;
        if (accountOrMeterNo.length() != 12) {
            accNum = accountOrMeterNo.substring(0, 2) + "/" + accountOrMeterNo.substring(2, 4) + "/"
                    + accountOrMeterNo.substring(4, 6) + "/" + accountOrMeterNo.substring(6, 10) + "-" + accountOrMeterNo.substring(10);
        }
        try {
            String[] postResponse = service.requery(accNum);
            if (postResponse[0].equals("200")) {
                JsonObject responseObj = new JsonParser().parse(postResponse[1]).getAsJsonObject();
                //JsonObject mainToken = new JsonObject();
                //JsonObject otherToken = new JsonObject();
                //  System.out.println("Double Token :: stdToken|bsstToken :: " + tokenField);
                //String firstToken = responseObj.get("token").getAsString();
                String vat = responseObj.get("vat").getAsString();
                String amt = responseObj.get("amountPaid").getAsString();
                String unit = responseObj.get("units").getAsString();
                String respcode = responseObj.get("responseCode").getAsString();
                String ref = responseObj.get("transactionRef").getAsString();
                String extRef = responseObj.get("transactionId").getAsString();
                String tarrifCode = responseObj.get("tarrifCode").getAsString();
                //String secondToken = tokenField.substring(index + 1);

//                mainToken.addProperty("unit", unit); //KILOWATT
//                mainToken.addProperty("amount", amt); //AMOUNT
//                mainToken.addProperty("vat", vat); //TAX
//                mainToken.addProperty("fixedCharge", ""); //FIXED CHARGE
//                mainToken.addProperty("token", firstToken); //TOKEN
                requeryDataObj.addProperty("errorCode", respcode);
                requeryDataObj.addProperty("uniqueTransId", ref);
                requeryDataObj.addProperty("externalReference", extRef);
                requeryDataObj.addProperty("tarrifCode", tarrifCode);
                //requeryDataObj.add("mainToken", mainToken);
                requeryDataObj.addProperty("responseCode", "00");
                requeryDataObj.addProperty("responseDesc", responseObj.get("responseMessage").getAsString());
            } else {
                requeryDataObj.addProperty("errorCode", postResponse[0]);
                requeryDataObj.addProperty("responseCode", "06");
                requeryDataObj.addProperty("responseDesc", "");
            }
        } catch (Exception ex) {
            requeryDataObj.addProperty("errorCode", "");
            requeryDataObj.addProperty("responseCode", "56");
            requeryDataObj.addProperty("responseDesc", "Timed out");
            log.error("Transaction requesry Failure", ex);
        }
        return new ElectricityProcessResponse();
    }

//    public ElectricityProcessResponse doPrePaidTransactionReversal(ElectricityReProcessRequest electricityReProcessRequest) {
//        String uniqueTransId = electricityReProcessRequest.getReference();
//        String accountOrMeterNo = electricityReProcessRequest.getAccount();
//        JsonObject reversalDataObj = new JsonObject();
//        reversalDataObj.addProperty("requestType", "Reversal");
//        reversalDataObj.addProperty("disco", "ENU");
//        reversalDataObj.addProperty("accountType", "Postpaid");
//        reversalDataObj.addProperty("uniqueTransId", electricityReProcessRequest.getReference());
//        reversalDataObj.addProperty("accountNumber", electricityReProcessRequest.getAccount());
//        if (accountOrMeterNo.length() != 12) {
//            reversalDataObj.addProperty("errorCode", "");
//            reversalDataObj.addProperty("responseCode", "06");
//            reversalDataObj.addProperty("responseDesc", "Invalid Account Number");
//            return new ElectricityProcessResponse();
//        }
//        String accNum = accountOrMeterNo.substring(0, 2) + "/" + accountOrMeterNo.substring(2, 4) + "/" + accountOrMeterNo.substring(4, 6) + "/" + accountOrMeterNo.substring(6, 10) + "-" + accountOrMeterNo.substring(10);
//        log.info("REVERSAL REQUEST ==== " + uniqueTransId);
//        String[] postResponse;
//        try {
//            postResponse = service.reversal(accNum);
//            log.info("RESPONSE CODE ==== " + postResponse[0]);
//
//            if (postResponse[0].equalsIgnoreCase("200")) {
//                JsonObject responseObj = new JsonParser().parse(postResponse[1]).getAsJsonObject();
//                String respcode = responseObj.get("responseCode").getAsString();
//                String responseMessage = responseObj.get("responseMessage").getAsString();
//                String receipt = responseObj.get("transactionId").getAsString();
//                //String reference = responseObj.get("transactionRef").getAsString();
//                reversalDataObj.addProperty("uniqueTransId", uniqueTransId);
//                reversalDataObj.addProperty("accountNumber", accountOrMeterNo);
//                reversalDataObj.addProperty("externalReference", receipt);
//                reversalDataObj.addProperty("errorCode", respcode);
//                reversalDataObj.addProperty("responseCode", "00");
//                reversalDataObj.addProperty("responseDesc", responseMessage);
//            } else {
//                reversalDataObj.addProperty("errorCode", postResponse[0]);
//                reversalDataObj.addProperty("responseCode", "06");
//                reversalDataObj.addProperty("responseDesc", "");
//            }
//        } catch (Exception ex) {
//            reversalDataObj.addProperty("errorCode", "");
//            reversalDataObj.addProperty("responseCode", "56");
//            reversalDataObj.addProperty("responseDesc", "Timed out");
//        }
//        return new ElectricityProcessResponse();
//    }

    public ElectricityProcessResponse doPrePaidTransactionRequery(ElectricityReQueryRequest electricityReProcessRequest) {
        String accountOrMeterNo = electricityReProcessRequest.getPayerId();
        JsonObject requeryDataObj = new JsonObject();
        requeryDataObj.addProperty("requestType", "Requery");
        requeryDataObj.addProperty("disco", "ENU");
        requeryDataObj.addProperty("accountType", "Prepaid");
        requeryDataObj.addProperty("uniqueTransId", electricityReProcessRequest.getReference());
        requeryDataObj.addProperty("accountNumber", electricityReProcessRequest.getPayerId());
        String accNum = accountOrMeterNo;//.substring(0, 2) + "/" + accountOrMeterNo.substring(2, 4) + "/" + accountOrMeterNo.substring(4, 6) + "/" + accountOrMeterNo.substring(6, 10) + "-" + accountOrMeterNo.substring(10);
        if (accountOrMeterNo.length() != 12) {
            requeryDataObj.addProperty("errorCode", "");
            requeryDataObj.addProperty("responseCode", "06");
            requeryDataObj.addProperty("responseDesc", "Invalid Account Number");
            return new ElectricityProcessResponse();
        }
        try {
            String[] postResponse = service.requery(accNum);
            if (postResponse[0].equals("200")) {
                JsonObject responseObj = new JsonParser().parse(postResponse[1]).getAsJsonObject();
                JsonObject mainToken = new JsonObject();
                //JsonObject otherToken = new JsonObject();
                //  System.out.println("Double Token :: stdToken|bsstToken :: " + tokenField);
                String firstToken = responseObj.get("token").getAsString();
                String vat = responseObj.get("vat").getAsString();
                String amt = responseObj.get("amountPaid").getAsString();
                String unit = responseObj.get("units").getAsString();
                String respcode = responseObj.get("responseCode").getAsString();
                String ref = responseObj.get("transactionRef").getAsString();
                String extRef = responseObj.get("transactionId").getAsString();
                //String secondToken = tokenField.substring(index + 1);

                mainToken.addProperty("unit", unit); //KILOWATT
                mainToken.addProperty("amount", amt); //AMOUNT
                mainToken.addProperty("vat", vat); //TAX
                mainToken.addProperty("fixedCharge", ""); //FIXED CHARGE
                mainToken.addProperty("token", firstToken); //TOKEN
                requeryDataObj.addProperty("errorCode", respcode);
                requeryDataObj.addProperty("uniqueTransId", ref);
                requeryDataObj.addProperty("externalReference", extRef);
                requeryDataObj.add("mainToken", mainToken);
                requeryDataObj.addProperty("responseCode", "00");
                requeryDataObj.addProperty("responseDesc", responseObj.get("responseMessage").getAsString());
                //return requeryDataObj.toString();
            } else {
                requeryDataObj.addProperty("errorCode", postResponse[0]);
                requeryDataObj.addProperty("responseCode", "06");
                requeryDataObj.addProperty("responseDesc", "");
            }
        } catch (Exception ex) {
            requeryDataObj.addProperty("errorCode", "");
            requeryDataObj.addProperty("responseCode", "56");
            requeryDataObj.addProperty("responseDesc", "Timed out");
            log.error("Transaction requery", ex);
        }
        return new ElectricityProcessResponse();

    }

    public void removeCustomerFromRedis(String meter, String account, String tarrif, String tarrif_desc){

                log.info(">>>>>>>>>>>>>>>>>>>>>>>>> remove customer from redis");
        try {
            if(meter != null) {
                redisService.removeAccount(meter);
            }
           if(account != null) {
               redisService.removeAccount(account);
           }
            if(tarrif != null) {
                redisService.removeAccount(tarrif);
            }
            if(tarrif_desc != null){
                redisService.removeAccount(tarrif_desc);
            }
        }catch (Exception e){

        }
    }
}
