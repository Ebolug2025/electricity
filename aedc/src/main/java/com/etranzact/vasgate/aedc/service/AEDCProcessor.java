package com.etranzact.vasgate.aedc.service;

import com.etranzact.vasgate.aedc.action.MeterType;
import com.etranzact.vasgate.aedc.dto.*;
import com.etranzact.vasgate.aedc.model.Customer;
import com.etranzact.vasgate.aedc.util.RedisService;
import com.etranzact.vasgate.electricity.phcnnode.PHCNNode;
import com.etranzact.vasgate.electricity.phcnnode.dto.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.etranzact.vasgate.aedc.action.StatusMessage.INVALID_METER_TYPE;

@Slf4j
@Service
@Component("phcnabj")
public class AEDCProcessor extends PHCNNode {

    public static String grantType;
    public static String prepaidUsername;
    public static String postpaidUsername;

    public static String prepaidPassword;
    public static String postpaidPassword;

    public static String baseUrl;

    private static String clientSecret;
    private static String email;

    private static String redisUrl;

    private static String abj_02USD;

    private static String abj_02POS;

    private static String abj_Mobile;

    private static String abj_switch;

    private static String abj_payoutlet;

    private static String abj_web;

    private static String abj_ussd;

    private static Properties prop = new Properties();

    @Autowired
    RedisService redisService;

    static {
        try {
            prop.load(new FileInputStream(new File("cfg/phcndb-config.properties")));
            // String mode = prop.getProperty("RUNNING_MODE");
            baseUrl = prop.getProperty("ABJ_BASEURL");

            grantType = prop.getProperty("ABJ_GRANT_TYPE");
            prepaidUsername = prop.getProperty("ABJ_USERNAME_PREPAID");
            postpaidUsername = prop.getProperty("ABJ_USERNAME_POSTPAID");

            prepaidPassword = prop.getProperty("ABJ_PASSWORD_PREPAID");
            postpaidPassword = prop.getProperty("ABJ_PASSWORD_POSTPAID");

            clientSecret = prop.getProperty("ABJ_CLIENT_SECRET");
            email = prop.getProperty("ABJ_EMAIL");
            redisUrl = prop.getProperty("REDIS_URL");

            abj_02USD = prop.getProperty("Abj_02USD");
            abj_02POS = prop.getProperty("Abj_02POS");
            abj_switch = prop.getProperty("Abj_SWITCH");
            abj_Mobile = prop.getProperty("Abj_Mobile");
            abj_payoutlet = prop.getProperty("Abj_PAYOUTLET");
            abj_web = prop.getProperty("Abj_WEB");
            abj_ussd = prop.getProperty("Abj_USSD");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private AEDCService service;

//    public AEDCProcessor(Logger log) {
//        logger = log;
//        logger.info("config data :: " + prepaidUsername + "-" + postpaidUsername + "-" + grantType + "-" + baseUrl + "-" + "-" + clientSecret);
//        this.service = new AEDCService(prepaidUsername, postpaidUsername, prepaidPassword, postpaidPassword, grantType, baseUrl, clientSecret, log, redisUrl);
//    }
    public AEDCProcessor(){
        log.info("config data :: " + prepaidUsername + "-" + postpaidUsername + "-" + grantType + "-" + baseUrl + "-" + "-" + clientSecret);
        this.service = new AEDCService(prepaidUsername, postpaidUsername, prepaidPassword, postpaidPassword, grantType, baseUrl, clientSecret, redisUrl);
    }

    @Override
    public ElectricityQueryResponse query(ElectricityQueryRequest electricityQueryRequest) {

        Gson gson = new Gson();
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> the request sent for abuja electricity distribution is: "+gson.toJson(electricityQueryRequest));

        if(electricityQueryRequest.getType().equalsIgnoreCase("1")){

            return doPrepaidCustomerInfo(electricityQueryRequest);
           // return prepaidQuery(electricityQueryRequest);
        } else if (electricityQueryRequest.getType().equalsIgnoreCase("2")) {

           // return postpaidQuery(electricityQueryRequest);
            return doPostPaidCustomerInfo(electricityQueryRequest);
        }

        ElectricityQueryResponse electricityQueryResponse = new ElectricityQueryResponse();
        electricityQueryResponse.setResponseDesc(INVALID_METER_TYPE.responseCode);
        electricityQueryResponse.setResponseDesc(INVALID_METER_TYPE.responseMsg);
        electricityQueryResponse.setAccountNumber(electricityQueryRequest.getPayerId());
        return electricityQueryResponse;
    }

    @Override
    public ElectricityProcessResponse process(ElectricityProcessRequest electricityProcessRequest) {
        if(electricityProcessRequest.getType().equalsIgnoreCase("1")){

            return doPrepaidTransactionPosting(electricityProcessRequest);
        }else if(electricityProcessRequest.getType().equalsIgnoreCase("2")){

            return doPostPaidTransactionPosting(electricityProcessRequest);
        }
        return null;
    }

    @Override
    public ElectricityProcessResponse reQuery(ElectricityReQueryRequest electricityReQueryRequest) {
        return null;
    }

    @Override
    public PingResponse ping(ElectricityQueryRequest electricityQueryRequest) {
        Gson gson = new Gson();
        PingResponse pingResponse = new PingResponse();
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>the request sent for ping is: "+gson.toJson(electricityQueryRequest));

        electricityQueryRequest.setPayerId(MeterType.PREPAID.toString());
        ElectricityQueryResponse electricityQueryResponse = query(electricityQueryRequest);

        if(electricityQueryResponse.getResponseCode().equalsIgnoreCase("00")){

            pingResponse.setCode("00");
            pingResponse.setMessage("success");
            return pingResponse;
        }else{
            pingResponse.setCode("01");
            pingResponse.setMessage("failed");
            return pingResponse;
        }

    }

//    public ElectricityQueryResponse prepaidQuery(ElectricityQueryRequest electricityQueryRequest){
//
//        Gson gson = new Gson();
//        log.info(">>>>>>>>>>>>>>>>>>>>>>>>> the request sent for abuja prepaid query transaction is: "+gson.toJson(electricityQueryRequest));
//    }
//
//    public ElectricityQueryResponse postpaidQuery(ElectricityQueryRequest electricityQueryRequest){
//
//        Gson gson = new Gson();
//        log.info(">>>>>>>>>>>>>>>>>>>>>>>>> the request sent for prepaid query transaction is: ");
//    }

    public ElectricityQueryResponse doPrepaidCustomerInfo(ElectricityQueryRequest electricityQueryRequest){
        String result = "";
        String accountOrMeterNo = electricityQueryRequest.getPayerId();
        String uniqueTransId = electricityQueryRequest.getReference();
        JsonObject verificationDataObj = new JsonObject();

        verificationDataObj.addProperty("requestType", "verification");
        verificationDataObj.addProperty("disco", "ABJ");
        verificationDataObj.addProperty("accountType", "prepaid");
        verificationDataObj.addProperty("uniqueTransId", uniqueTransId);
        verificationDataObj.addProperty("accountNumber", accountOrMeterNo);

        try {
            String[] postResponse = verifyUser(accountOrMeterNo, prepaidUsername, "prepaid");
            log.info("RESPONSE CODE ==== " + postResponse[0]);

            if (postResponse[0].equals("200")) {
                log.info("ABJ PREPAID VERIFICATION RESPONSE : " + postResponse[1]);
                Customer[] customers = new Gson().fromJson(postResponse[1], Customer[].class);
                Customer customer = customers[0];
                //RedisService redisService = new RedisService(redisUrl);
                redisService.setCustomer(customer.getMeterSerial(), postResponse[1]);
                redisService.setCustomer(customer.getAccount(), postResponse[1]);
                redisService.setCustomer(accountOrMeterNo, postResponse[1]);// to handle old account cases

                //REVAMPED TO USE REDIS INSTEAD
//                Session session = SessionFactory.getSingleton();
//                session.updateCustomers(customer.getMeterSerial(), postResponse[1]);
//                session.updateCustomers(customer.getAccount(), postResponse[1]);
//                session.updateCustomers(accountOrMeterNo, postResponse[1]); // to handle old account cases

                verificationDataObj.addProperty("customerAddress", customer.getServiceAddress());
                verificationDataObj.addProperty("businessUnit", "ABJ BUSINESS UNIT");

                verificationDataObj.addProperty("state", " ");
                verificationDataObj.addProperty("minimumPurchase", "1000");
                try {
                    verificationDataObj.addProperty("customerArrears", customer.getAccountBalance());
                } catch (Exception ex) {
                    log.info("Customer Arrears is empty================= " + ex.getMessage());
                }
                verificationDataObj.addProperty("externalReference", uniqueTransId);

                verificationDataObj.addProperty("errorCode", postResponse[0]);
                verificationDataObj.addProperty("responseCode", "00");
                verificationDataObj.addProperty("responseDesc", "Success");
                verificationDataObj.addProperty("minVendAmount", "1000");
                verificationDataObj.addProperty("maxVendAmount", " ");
                verificationDataObj.addProperty("tarrif", customer.getTariffDescription());
                verificationDataObj.addProperty("meterSerial", customer.getMeterSerial());
                verificationDataObj.addProperty("account", customer.getMeterSerial());
                verificationDataObj.addProperty("accountBalance", customer.getAccountBalance());
                verificationDataObj.addProperty("customerName", customer.getName());

            } else {
                FailResponse failResponse = new Gson().fromJson(postResponse[1], FailResponse.class);
                verificationDataObj.addProperty("responseCode", "06");
                String msg = failResponse.getMsgDeveloper() + "-" + failResponse.getMsgUser();
                verificationDataObj.addProperty("responseDesc", msg);
            }

        } catch (Exception e) {
            log.error("Error : ", e);
            verificationDataObj.addProperty("responseCode", "56");
            verificationDataObj.addProperty("responseDesc", e.getMessage());
        }

        result = verificationDataObj.toString();
        return new ElectricityQueryResponse();
    }

    public ElectricityQueryResponse doPostPaidCustomerInfo(ElectricityQueryRequest electricityQueryRequest){
        String result = "";
        String accountOrMeterNo = electricityQueryRequest.getPayerId();
        String uniqueTransId = electricityQueryRequest.getReference();
        JsonObject verificationDataObj = new JsonObject();

        verificationDataObj.addProperty("requestType", "verification");
        verificationDataObj.addProperty("disco", "ABJ");
        verificationDataObj.addProperty("accountType", "postpaid");
        verificationDataObj.addProperty("uniqueTransId", uniqueTransId);
        verificationDataObj.addProperty("accountNumber", accountOrMeterNo);

        try {

            String[] postResponse = verifyUser(accountOrMeterNo, postpaidUsername, "postpaid");
            log.info("RESPONSE CODE ==== " + postResponse[0]);

            if (postResponse[0].equals("200")) {
                log.info("ABJ POSTPAID VERIFICATION RESPONSE : " + postResponse[1]);

                log.info("ABJ POSTPAID VERIFICATION RESPONSE : " + postResponse[1]);
                Customer[] customers = new Gson().fromJson(postResponse[1], Customer[].class);
                Customer customer = customers[0];
               // RedisService redisService = new RedisService(redisUrl);
                redisService.setCustomer(customer.getMeterSerial(), postResponse[1]);
                redisService.setCustomer(customer.getAccount(), postResponse[1]);
                redisService.setCustomer(accountOrMeterNo, postResponse[1]);// to handle old account cases

                //REVAMPED TO USE REDIS INSTEAD
                //             Session session = SessionFactory.getSingleton();
//                session.updateCustomers(customer.getMeterSerial(), postResponse[1]);
//                session.updateCustomers(customer.getAccount(), postResponse[1]);
//                session.updateCustomers(accountOrMeterNo, postResponse[1]); // to handle old account cases

                verificationDataObj.addProperty("customerName", customer.getName());
                verificationDataObj.addProperty("customerAddress", customer.getServiceAddress());
                verificationDataObj.addProperty("tarrif", customer.getTariffDescription());
                verificationDataObj.addProperty("meter", customer.getMeterSerial());

                try {
                    verificationDataObj.addProperty("customerArrears", customer.getAccountBalance());
                } catch (Exception ex) {
                    log.info("Customer Arrears is empty================= " + ex.getMessage());
                }
                verificationDataObj.addProperty("minimumPurchase", "100");

                verificationDataObj.addProperty("businessUnit", "ABJ BUSINESS UNIT");
                verificationDataObj.addProperty("responseCode", "00");
                verificationDataObj.addProperty("responseDesc", "Successful");

            } else {
                FailResponse failResponse = new Gson().fromJson(postResponse[1], FailResponse.class);

                verificationDataObj.addProperty("errorCode", failResponse.getCode());
                verificationDataObj.addProperty("responseCode", "56");
                verificationDataObj.addProperty("responseDesc", this.service.buildErrorMessage(failResponse));
            }
        } catch (Exception e) {
            log.error("Error : ", e);
            verificationDataObj.addProperty("responseCode", "56");
            verificationDataObj.addProperty("responseDesc", "Account number details not found");
        }
        result = verificationDataObj.toString();
        return new ElectricityQueryResponse();
    }

    private ElectricityProcessResponse doPrepaidTransactionPosting(ElectricityProcessRequest electricityProcessRequest) {
        String amount = String.valueOf(electricityProcessRequest.getAmount());
        String accountOrMeterNo = electricityProcessRequest.getPayerId();
        String uniqueTransId = electricityProcessRequest.getReference();
        String paymentChannel = electricityProcessRequest.getPaymentChannel();
        String mobileNo = electricityProcessRequest.getMobile();

        JsonObject paymentDataObj = new JsonObject();
        paymentDataObj.addProperty("requestType", "payment");
        paymentDataObj.addProperty("disco", "ABJ");
        paymentDataObj.addProperty("accountType", "prepaid");
        paymentDataObj.addProperty("uniqueTransId", uniqueTransId);
        paymentDataObj.addProperty("accountNumber", accountOrMeterNo);
        try {
            FailResponse failResponse = null;
            PaymentResponse paymentResponseObj = null;
            //RedisService redisService = new RedisService(redisUrl);
            //String strCustomers = redisService.getInstance().get(accountOrMeterNo);
            Customer[] customers = new Gson().fromJson((String) redisService.getInstance().opsForValue().get(accountOrMeterNo), Customer[].class);
            // Session session = SessionFactory.getSingleton();
            //String strCustomers = session.getCustomers().get(accountOrMeterNo);
            Customer customer = customers[0];


            String[] credentials = this.service.getUsernamePassword("prepaid");
            String username = credentials[0];
            String password = credentials[1];

            TokenResponse tokenResponse = this.service.getAccessToken(username, password);
            String[] calculatePaymentResponse = this.service.calculatePayment(tokenResponse, amount, username, customer.getMeterSerial());

            String[] paymentResponse = this.service.vendPin(
                    accountOrMeterNo, amount, uniqueTransId, mobileNo, email, "prepaid", customer
                    , tokenResponse, credentials[0], calculatePaymentResponse, paymentChannel, getChannel(uniqueTransId, paymentChannel)
            );

            if (paymentResponse.length == 0 || !paymentResponse[0].equals("201")) {
                log.info("Payment Failed! " + Arrays.toString(paymentResponse));
                failResponse = new Gson().fromJson(paymentResponse[1], FailResponse.class);

                paymentDataObj.addProperty("errorCode", failResponse.getCode());
                paymentDataObj.addProperty("responseCode", "06");

                paymentDataObj.addProperty("responseDesc", this.service.buildErrorMessage(failResponse));

            } else {
                paymentResponseObj = new Gson().fromJson(paymentResponse[1], PaymentResponse.class);
                log.info("ABJ PREPAID PAYMENT RESPONSE : " + paymentResponseObj);

                String unitsTopUp = paymentResponseObj.getUnitsTopUp().toString();
                String keyDataSGC = paymentResponseObj.getKeyDataSGC() + "";
                String keyDataTI = paymentResponseObj.getKeyDataTI() + "";
                String keyDataKRN = paymentResponseObj.getKeyDataKRN() + "";

                String vat = this.service.retrieveVAT(paymentResponseObj);

                JsonObject mainToken = new JsonObject();
                String firstToken = "";
                if (paymentResponseObj.getListtoken() != null) {
                    firstToken = fetchTokens(paymentResponseObj.getListtoken());
                }

                if (paymentResponseObj.getUnitsTopUp().size() > 0)
                    mainToken.addProperty("unit", paymentResponseObj.getUnitsTopUp().get(0).getUnits());

                mainToken.addProperty("unitsTopUp", unitsTopUp);
                mainToken.addProperty("amount", paymentResponseObj.getTotalPayment());
                mainToken.addProperty("vat", vat);
                mainToken.addProperty("fixedCharge", " ");
                mainToken.addProperty("token", firstToken);
                mainToken.addProperty("keyDataSGC", keyDataSGC);
                mainToken.addProperty("keyDataTI", keyDataTI);
                mainToken.addProperty("keyDataKRN", keyDataKRN);
                paymentDataObj.add("mainToken", (JsonElement) mainToken);

                paymentDataObj.addProperty("errorCode", "00");
                paymentDataObj.addProperty("externalReference", paymentResponseObj.getReceipt());
                paymentDataObj.addProperty("responseCode", "00");
                paymentDataObj.addProperty("responseDesc", "Successful");
            }


            System.out.println(paymentDataObj);
            // clearSession(session, customer, accountOrMeterNo);
            redisService.getInstance().opsForValue().getAndDelete(accountOrMeterNo);

        } catch (Exception ex) {
            log.error("Transaction Posting Failure", ex);
            paymentDataObj.addProperty("responseCode", "06");
            paymentDataObj.addProperty("responseDesc", ex.getMessage());
            String result = paymentDataObj.toString();
        }
        return new ElectricityProcessResponse();
    }

    private ElectricityProcessResponse doPostPaidTransactionPosting(ElectricityProcessRequest electricityProcessRequest) {

        String response = "";
        String amount = String.valueOf(electricityProcessRequest.getAmount());
        String accountOrMeterNo = electricityProcessRequest.getPayerId();
        String uniqueTransId = electricityProcessRequest.getReference();
        String paymentChannel = electricityProcessRequest.getPaymentChannel();
        String mobile = electricityProcessRequest.getMobile();

        Long posted_on = System.currentTimeMillis();

        JsonObject paymentDataObj = new JsonObject();
        paymentDataObj.addProperty("requestType", "payment");
        paymentDataObj.addProperty("disco", "ABJ");
        paymentDataObj.addProperty("accountType", "postpaid");
        paymentDataObj.addProperty("uniqueTransId", electricityProcessRequest.getReference());
        paymentDataObj.addProperty("accountNumber", electricityProcessRequest.getPayerId());

        FailResponse failResponse = null;
        PaymentResponse paymentResponseObj = null;
        try {
            //RedisService redisService = new RedisService(redisUrl);
            //String strCustomers = null;
            //strCustomers = redisService.getInstance().get(accountOrMeterNo);
            Customer[] customers = new Gson().fromJson((String) redisService.getInstance().opsForValue().get(accountOrMeterNo), Customer[].class);

            // Session session = SessionFactory.getSingleton();
            // String strCustomers = session.getCustomers().get(accountOrMeterNo);
           // Customer[] customers = new Gson().fromJson(strCustomers, Customer[].class);
            Customer customer = customers[0];


            String[] credentials = this.service.getUsernamePassword("postpaid");
            String username = credentials[0];
            String password = credentials[1];

            TokenResponse tokenResponse = this.service.getAccessToken(username, password);
            String[] calculatePaymentResponse = this.service.calculatePayment(tokenResponse, amount, username, customer.getMeterSerial());

            String[] paymentResponse = this.service.vendPin(
                    accountOrMeterNo, amount, uniqueTransId, mobile, email, "postpaid", customer,
                    tokenResponse, username, calculatePaymentResponse, paymentChannel, getChannel(uniqueTransId, paymentChannel)
            );

            if (paymentResponse.length > 0 && !paymentResponse[0].equals("201")) {
                log.info("Payment Failed! " + Arrays.toString(paymentResponse));
                failResponse = new Gson().fromJson(paymentResponse[1], FailResponse.class);

                paymentDataObj.addProperty("errorCode", failResponse.getCode());
                paymentDataObj.addProperty("responseCode", "06");

                paymentDataObj.addProperty("responseDesc", this.service.buildErrorMessage(failResponse));

            } else {
                PaymentValueResponse paymentValueResponse = new Gson().fromJson(calculatePaymentResponse[1],
                        PaymentValueResponse.class);

                paymentResponseObj = new Gson().fromJson(paymentResponse[1], PaymentResponse.class);
                log.info("ABJ POSTPAID PAYMENT RESPONSE : " + paymentResponseObj);

                String firstToken = "";
                if (paymentResponseObj.getListtoken() != null) {
                    firstToken = fetchTokens(paymentResponseObj.getListtoken());
                }

                paymentDataObj.addProperty("token", firstToken);
                paymentDataObj.addProperty("receipt_no", paymentResponseObj.getReceipt());
                paymentDataObj.addProperty("reference", uniqueTransId);
                paymentDataObj.addProperty("customer_no", mobile);
                paymentDataObj.addProperty("customer_name", paymentResponseObj.getCustomerName());
                paymentDataObj.addProperty("customer_address", customer.getServiceAddress());
                paymentDataObj.addProperty("vendor", paymentResponseObj.getIdVendor());
                paymentDataObj.addProperty("amount", paymentResponseObj.getTotalPayment());
                paymentDataObj.addProperty("vref", paymentResponseObj.getRequestID());
                paymentDataObj.addProperty("reprint", paymentResponseObj.getRequestID());
                paymentDataObj.addProperty("outstanding", paymentResponseObj.getDebtPayment());
                paymentDataObj.addProperty("tariff", paymentResponseObj.getTariffDescription());
                paymentDataObj.addProperty("last_payment_date", paymentValueResponse.getLastPaymentDate());
                paymentDataObj.addProperty("last_payment", paymentValueResponse.getAmountLast());
                paymentDataObj.addProperty("responseCode", "00");
                paymentDataObj.addProperty("responseDesc", "Successful");

                paymentDataObj.addProperty("externalReference", paymentResponseObj.getReceipt());
                paymentDataObj.addProperty("businessUnit", "ABJ BUSINESS UNIT");
            }


            System.out.println(response);
            // clearSession(session, customer, accountOrMeterNo);
            redisService.getInstance().opsForValue().get(accountOrMeterNo);

        } catch (Exception e) {
            log.error("Transaction Posting Failure", e);
            paymentDataObj.addProperty("responseCode", "06");
            paymentDataObj.addProperty("responseDesc", e.getMessage());
        }
        response = paymentDataObj.toString();
        return new ElectricityProcessResponse();
    }

    public ElectricityQueryResponse doPrePaidTransactionReversal(ElectricityReQueryRequest electricityReQueryRequest) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ElectricityQueryResponse doPrePaidTransactionRequery(ElectricityReQueryRequest electricityReQueryRequest) {
        String accountOrMeterNo = electricityReQueryRequest.getPayerId();

        JsonObject requeryDataObj = new JsonObject();
        requeryDataObj.addProperty("requestType", "payment");
        requeryDataObj.addProperty("disco", "ABJ");
        requeryDataObj.addProperty("accountType", "requery");
        String uniqueTransId = electricityReQueryRequest.getReference();

        requeryDataObj.addProperty("uniqueTransId", uniqueTransId);
        requeryDataObj.addProperty("accountNumber", electricityReQueryRequest.getPayerId());

        FailResponse failResponse = null;
        PaymentResponse pdResponse = null;
        try {
            com.etranzact.vasgate.aedc.dto.PaymentDetailsRequest paymentDetailsRequest = new com.etranzact.vasgate.aedc.dto.PaymentDetailsRequest();
            String usernameParts[] = prepaidUsername.split("#");
            paymentDetailsRequest.setIdVendor(usernameParts[0])
                    .setCodUser(usernameParts[0])
                    .setTransactionId(uniqueTransId);

            String[] paymentDetailsResponse = this.service.reQueryPrepaidTransaction(paymentDetailsRequest, "prepaid");

            if (paymentDetailsResponse.length == 0 || !paymentDetailsResponse[0].equals("200")) {
                log.info("Payment Failed! " + Arrays.toString(paymentDetailsResponse));
                failResponse = new Gson().fromJson(paymentDetailsResponse[1], FailResponse.class);

                requeryDataObj.addProperty("errorCode", failResponse.getCode());
                requeryDataObj.addProperty("responseCode", "06");

                requeryDataObj.addProperty("responseDesc", this.service.buildErrorMessage(failResponse));

            } else {
                pdResponse = new Gson().fromJson(paymentDetailsResponse[1], PaymentResponse.class);
                log.info("ABJ PREPAID PAYMENT RESPONSE : " + pdResponse);

                JsonObject mainToken = new JsonObject();

                String firstToken = pdResponse.getListtoken().toString();

                String unitsTopUp = pdResponse.getUnitsTopUp().toString();
                String keyDataSGC = pdResponse.getKeyDataSGC() + "";
                String keyDataTI = pdResponse.getKeyDataTI() + "";
                String keyDataKRN = pdResponse.getKeyDataKRN() + "";

                String extRef = pdResponse.getRequestID();
                String vat = this.service.retrieveVAT(pdResponse);

                mainToken.addProperty("unitsTopUp", unitsTopUp);
                if (pdResponse.getUnitsTopUp().size() > 0)
                    mainToken.addProperty("unit", pdResponse.getUnitsTopUp().get(0).getUnits());

                mainToken.addProperty("amount", pdResponse.getTotalPayment());
                mainToken.addProperty("vat", vat);
                mainToken.addProperty("fixedCharge", " ");
                mainToken.addProperty("token", firstToken);
                mainToken.addProperty("keyDataSGC", keyDataSGC);
                mainToken.addProperty("keyDataTI", keyDataTI);
                mainToken.addProperty("keyDataKRN", keyDataKRN);

                requeryDataObj.addProperty("errorCode", "00");
                requeryDataObj.addProperty("externalReference", pdResponse.getReceipt());
                requeryDataObj.add("mainToken", (JsonElement) mainToken);

                requeryDataObj.addProperty("responseCode", "00");
                requeryDataObj.addProperty("responseDesc", "Successful");
            }
        } catch (Exception ex) {
            log.error("CustomerInfo Failure", ex);
        }
        String result  = requeryDataObj.toString();
        return new ElectricityQueryResponse();
    }

    public ElectricityQueryResponse doPostPaidTransactionRequery(ElectricityReQueryRequest electricityReQueryRequest) {
        String accountOrMeterNo = electricityReQueryRequest.getPayerId();

        JsonObject requeryDataObj = new JsonObject();
        requeryDataObj.addProperty("requestType", "payment");
        requeryDataObj.addProperty("disco", "ABJ");
        requeryDataObj.addProperty("accountType", "requery");
        String uniqueTransId = electricityReQueryRequest.getReference();
        String mobile = electricityReQueryRequest.getMobile();
        requeryDataObj.addProperty("uniqueTransId", uniqueTransId);
        requeryDataObj.addProperty("accountNumber", accountOrMeterNo);

        FailResponse failResponse = null;
        PaymentResponse pdResponse = null;
        try {
            PaymentDetailsRequest paymentDetailsRequest = new PaymentDetailsRequest();
            String usernameParts[] = prepaidUsername.split("#");
            paymentDetailsRequest.setIdVendor(usernameParts[0])
                    .setCodUser(usernameParts[0])
                    .setTransactionId(uniqueTransId);

            String[] paymentDetailsResponse = this.service.reQueryPrepaidTransaction(paymentDetailsRequest, "postpaid");

            if (paymentDetailsResponse.length == 0 || !paymentDetailsResponse[0].equals("201")) {
                log.info("Payment Failed! " + Arrays.toString(paymentDetailsResponse));
                failResponse = new Gson().fromJson(paymentDetailsResponse[1], FailResponse.class);

                requeryDataObj.addProperty("errorCode", failResponse.getCode());
                requeryDataObj.addProperty("responseCode", "06");

                requeryDataObj.addProperty("responseDesc", this.service.buildErrorMessage(failResponse));
            } else {

                pdResponse = new Gson().fromJson(paymentDetailsResponse[1], PaymentResponse.class);
                log.info("ABJ POSTPAID PAYMENT RESPONSE : " + pdResponse);

                String firstToken = "";
                if (pdResponse.getListtoken() != null) {
                    firstToken = pdResponse.getListtoken().toString();
                }

                String[] customerInfo = verifyUser(pdResponse.getMeterSerial(), postpaidUsername, "postpaid");
                Customer[] customers = new Gson().fromJson(customerInfo[1], Customer[].class);
                Customer customer = customers[0];

                requeryDataObj.addProperty("token", firstToken);
                requeryDataObj.addProperty("receipt_no", pdResponse.getReceipt());
                requeryDataObj.addProperty("reference", uniqueTransId);
                requeryDataObj.addProperty("customer_no", mobile);
                requeryDataObj.addProperty("customer_name", pdResponse.getCustomerName());
                requeryDataObj.addProperty("customer_address", customer.getServiceAddress());
                requeryDataObj.addProperty("vendor", pdResponse.getIdVendor());
                requeryDataObj.addProperty("amount", pdResponse.getTotalPayment());
                requeryDataObj.addProperty("vref", pdResponse.getRequestID());
                requeryDataObj.addProperty("reprint", pdResponse.getReceipt());
                requeryDataObj.addProperty("outstanding", pdResponse.getDebtPayment());
                requeryDataObj.addProperty("tariff", pdResponse.getTariffDescription());
                requeryDataObj.addProperty("last_payment_date", pdResponse.getPaymentDate());
                requeryDataObj.addProperty("last_payment", pdResponse.getTotalPayment());
                requeryDataObj.addProperty("responseCode", "00");
                requeryDataObj.addProperty("responseDesc", "Successful");

                requeryDataObj.addProperty("externalReference", pdResponse.getRequestID());
                requeryDataObj.addProperty("businessUnit", "ABJ BUSINESS UNIT");
            }
        } catch (Exception ex) {
            log.error("CustomerInfo Failure", ex);
        }
        String result  = requeryDataObj.toString();
        return new ElectricityQueryResponse();
    }

    public ElectricityQueryResponse doPostPaidTransactionReversal(ElectricityQueryRequest electricityQueryRequest) {
        throw new UnsupportedOperationException("Not supported yet.");
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

    private String[] verifyUser(String accountOrMeterNo, String username, String meterType) throws Exception {
        CustomerRequest customerRequest = new CustomerRequest();
        String[] userParts = username.split("#");
        String idVendor = userParts[0];
        String codUser = userParts[1];

        customerRequest.setIdVendor(idVendor);
        customerRequest.setCodUser(codUser);
        customerRequest.setValue(accountOrMeterNo);

        String[] postResponse = service.validateMeter(customerRequest, meterType);

        return postResponse;
    }

    public static void main(String args[]) {
        //AEDCProcessor p = new AEDCProcessor(logger);
        JsonObject transObj = new JsonObject();

    }

    //This channel update was done as requested by Business on 2/5/2024
    public int getChannel(String uniqueTransId, String paymentChannel) {
        int channel = 0;
        String channelKey = uniqueTransId.substring(0, 2);//02USDAJDHDIFFIDNDD  //02POSXDKJFMFJFKJFK
        if (!paymentChannel.startsWith("0") && channelKey.equals("09")) {
            channel = Integer.parseInt(paymentChannel);
        } else {
            if (channelKey.equals("01") || channelKey.equals("09")) {
                channel = Integer.parseInt(abj_switch);
            } else if (channelKey.equals("02")) {
                if (uniqueTransId.contains("02USD")) {
                    channel = Integer.parseInt(abj_02USD);
                } else if (uniqueTransId.contains("02POS")) {
                    channel = Integer.parseInt(abj_02POS);
                } else {
                    channel = Integer.parseInt(abj_Mobile);
                    //channel =  //mobile
                }
            } else if (channelKey.equals("03")) {
                channel = Integer.parseInt(abj_02POS);
            } else if (channelKey.equals("05")) {
                channel = Integer.parseInt(abj_payoutlet);

            } else if (channelKey.equals("11")) {
                channel = Integer.parseInt(abj_web);

            }  else {
                channel = Integer.parseInt("1");
            }
        }

        return channel;
    }
}
