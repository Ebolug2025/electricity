package com.etranzact.vasgate.electricity.kedc.service;

import com.etranzact.vasgate.electricity.kedc.actionmenu.EnumResponseMsg;
import com.etranzact.vasgate.electricity.kedc.dto.PaymentResponse;
import com.etranzact.vasgate.electricity.kedc.dto.VerificationResponse;
import com.etranzact.vasgate.electricity.kedc.utils.HttpUtil;
import com.etranzact.vasgate.electricity.phcnnode.PHCNNode;
import com.etranzact.vasgate.electricity.phcnnode.dto.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;

@Service
@Component("phcnkan")
public class KEDCProcessor extends PHCNNode {

    @Value("${KAN_BASE_URL}")
    private String baseUrl;

    @Value("${KAN_QUERY}")
    private String KAN_QUERY;

    @Value("${KAN_PROCESS}")
    private String KAN_PROCESS;

    @Value("${KAN_PING_NUMBER}")
    private String KAN_PING_NUMBER;

    @Value("${KAN_ACCESS_TOKEN}")
    private String KAN_ACCESS_TOKEN;

    @Value("${KAN_VENDOR_ID}")
    private String KAN_VENDOR_ID;

    private static final Logger log = LoggerFactory.getLogger(KEDCProcessor.class);

    @Override
    public ElectricityQueryResponse query(ElectricityQueryRequest electricityQueryRequest) {
        Gson gson = new Gson();
        log.info("================ the request sent for kano "+gson.toJson(electricityQueryRequest));
        String customerId = electricityQueryRequest.getPayerId();
        String accountType = electricityQueryRequest.getType();
        String uniqueTransId = electricityQueryRequest.getReference();
        JsonObject jsonObject = null;

        ElectricityQueryResponse electricityQueryResponse = null;
        if(accountType.equals("1")) {
            electricityQueryResponse = customerVerification(customerId, "prepaid", uniqueTransId);
        } else if (accountType.equals("2")) {
            electricityQueryResponse = customerVerification(customerId, "postpaid", uniqueTransId);
        }else {
             electricityQueryResponse = new ElectricityQueryResponse();
            electricityQueryResponse.setResponseDesc(EnumResponseMsg.INVALID_ACCOUNT_TYPE.responseMsg);
            electricityQueryResponse.setResponseCode(EnumResponseMsg.INVALID_ACCOUNT_TYPE.responseCode);
            return electricityQueryResponse;
        }
        return electricityQueryResponse;
    }

    @Override
    public ElectricityProcessResponse process(ElectricityProcessRequest electricityProcessRequest) {

        Gson gson = new Gson();
        log.info("=============================the request sent for payment "+gson.toJson(electricityProcessRequest));
        JsonObject paymentRequest = new JsonObject();
        String uniqueTransId = electricityProcessRequest.getReference();
        String paymentChannel = electricityProcessRequest.getPaymentChannel();
        String customerId = electricityProcessRequest.getPayerId();
        double amount = electricityProcessRequest.getAmount();
        ElectricityProcessResponse electricityProcessResponse = null;
        String accountType = null;
        if(electricityProcessRequest.getType().equals("1")){
            accountType = "prepaid";
        } else if (electricityProcessRequest.getType().equals("2")) {
            accountType = "postpaid";
        }else{
            electricityProcessResponse = new ElectricityProcessResponse();
            electricityProcessResponse.setResponseCode(EnumResponseMsg.INVALID_ACCOUNT_TYPE.responseCode);
            electricityProcessResponse.setResponseDesc(EnumResponseMsg.INVALID_ACCOUNT_TYPE.responseMsg);
            return electricityProcessResponse;
        }

        String paymentMethod = HttpUtil.getChannel( uniqueTransId,  paymentChannel).toLowerCase();

        paymentRequest.addProperty("payment_method",paymentMethod);
        paymentRequest.addProperty("amount",Double.valueOf(amount));
        paymentRequest.addProperty("type",accountType);
        paymentRequest.addProperty("transaction_reference",uniqueTransId);
        if(accountType.equalsIgnoreCase("prepaid")){
            paymentRequest.addProperty("meter_number",customerId);
        }else{
            paymentRequest.addProperty("account_number",customerId);
        }
        String strPaymentRequest = gson.toJson(paymentRequest);

        String  url = baseUrl + KAN_PROCESS;

        electricityProcessResponse = customPayment( uniqueTransId,accountType, url,  strPaymentRequest,  KAN_ACCESS_TOKEN);

        return electricityProcessResponse;
    }

    @Override
    public ElectricityProcessResponse reQuery(ElectricityReQueryRequest electricityReQueryRequest) {
        return null;
    }

    @Override
    public PingResponse ping(ElectricityQueryRequest electricityQueryRequest) {
        return null;
    }

    private ElectricityQueryResponse customerVerification(String customerId, String accountType, String uniqueTransId) {

        JsonObject verificationDataObj = new JsonObject();
        ElectricityQueryResponse electricityQueryResponse = new ElectricityQueryResponse();
        Gson gson = new Gson();

        if (customerId == null) {
            electricityQueryResponse.setResponseCode("06");
            electricityQueryResponse.setResponseDesc("Customer ID cannot be null");
            return electricityQueryResponse;
        }

        JsonObject jobAccountNumber = new JsonObject();
        if (customerId != null) {
            if (accountType.equals("postpaid")) {
                jobAccountNumber.addProperty("account_number", customerId);
            } else if (accountType.equals("prepaid")) {
                jobAccountNumber.addProperty("meter_number", customerId);
            }
        }

        // verificationRequest.setMeter_number(customerId);
        String strVerificationRequest = jobAccountNumber.toString();
        electricityQueryResponse.setRequestType("verification");
        electricityQueryResponse.setDisco("KAN");
        electricityQueryResponse.setCustomerType(accountType);
        //  verificationDataObj.addProperty("meter_number", customerId);
        electricityQueryResponse.setUniqueTransId(uniqueTransId);
        //  verificationDataObj.addProperty(accountType.equals("prepaid") ? "meterNumber" : "accountNumber", customerId);

        if (accountType.equals("prepaid")) {
            verificationDataObj.addProperty("meterNumber", customerId);
        } else {
            verificationDataObj.addProperty("accountNumber", customerId);
        }

        try {
            //    String request = baseUrl + "Identification/" + mercahntCode + "/" + customerId + "/" + accessToken + ";referencetype=" + referencetype + "?postpaid=" + postPre;
            String request = null;
            if(accountType.equalsIgnoreCase("prepaid")) {
                request = baseUrl + KAN_QUERY;
            }else {
                request = baseUrl + KAN_QUERY;
            }

            log.info("Request to KANO API :: " + request);

            //   String[] ret = HttpUtil.sendGet(request, "", "");
            String[] ret = HttpUtil.sendPost(request, strVerificationRequest, KAN_ACCESS_TOKEN, "", 3);
            log.info("Response from Kano :: " + ret[1]);
            if (ret[0].equalsIgnoreCase("200")) {

                VerificationResponse verResponse = gson.fromJson(ret[1], VerificationResponse.class);

                verificationDataObj.addProperty("success", verResponse.getSuccess());
                if(verResponse.getSuccess()) {
                    verificationDataObj.addProperty("message", verResponse.getMessage());
                    //verificationDataObj.addProperty("id", verResponse.getPayload().getMeter().getId());

                    verificationDataObj.addProperty("externalReference", "");
                    if (accountType.equalsIgnoreCase("prepaid")) {
                        electricityQueryResponse.setCustomerName(verResponse.getPayload().getMeter().getCustomer().getName());
                        electricityQueryResponse.setCustomerAddress(verResponse.getPayload().getMeter().getAddress());
                        electricityQueryResponse.setCustomerName(verResponse.getPayload().getMeter().getCustomer().getName());
                        electricityQueryResponse.setEmail(verResponse.getPayload().getMeter().getCustomer().getEmail());
                        electricityQueryResponse.setMobile(String.valueOf(verResponse.getPayload().getMeter().getCustomer().getPhone()));
                        electricityQueryResponse.setGender(verResponse.getPayload().getMeter().getCustomer().getGender());
                        electricityQueryResponse.setMinimumPurchase(String.valueOf(verResponse.getPayload().getMeter().getMinimum_purchase()));
                        electricityQueryResponse.setTariff(verResponse.getPayload().getMeter().getTariff());
                        electricityQueryResponse.setAccountStatus(verResponse.getPayload().getMeter().getAccount_status());
//                        verificationDataObj.addProperty("validated", verResponse.getPayload().getMeter().getCustomer().getValidated());
                        //verificationDataObj.addProperty("nin", verResponse.getPayload().getMeter().getCustomer().getNin());
//                        electricityQueryResponse("account_status", verResponse.getPayload().getMeter().getCustomer().getAccount_status());
//                        verificationDataObj.addProperty("region", verResponse.getPayload().getMeter().getCustomer().getRegion());
//                        verificationDataObj.addProperty("csp", verResponse.getPayload().getMeter().getCustomer().getCsp());
                    } else {
                        electricityQueryResponse.setCustomerName(verResponse.getPayload().getMeter().getCustomer().getName());
                        electricityQueryResponse.setCustomerAddress(verResponse.getPayload().getMeter().getAddress());
                        electricityQueryResponse.setCustomerName(verResponse.getPayload().getMeter().getCustomer().getName());
                        electricityQueryResponse.setEmail(verResponse.getPayload().getMeter().getCustomer().getEmail());
                        electricityQueryResponse.setMobile(String.valueOf(verResponse.getPayload().getMeter().getCustomer().getPhone()));
                        electricityQueryResponse.setGender(verResponse.getPayload().getMeter().getCustomer().getGender());
                        electricityQueryResponse.setMinimumPurchase(String.valueOf(verResponse.getPayload().getMeter().getMinimum_purchase()));
                        electricityQueryResponse.setTariff(verResponse.getPayload().getMeter().getTariff());
                        electricityQueryResponse.setAccountStatus(verResponse.getPayload().getMeter().getAccount_status());
                        electricityQueryResponse.setBusinessUnit(verResponse.getPayload().getMeter().getRegion());
//                        verificationDataObj.addProperty("region", verResponse.getPayload().getAccount().getRegion());
//                        verificationDataObj.addProperty("csp", verResponse.getPayload().getAccount().getCsp());
                    }

                    //verificationDataObj.addProperty("account_number", verResponse.getPayload().getMeter().getAccount_number());
                    //verificationDataObj.addProperty("meter_number", verResponse.getPayload().getMeter().getMeter_number());

                    electricityQueryResponse.setBusinessUnit(verResponse.getPayload().getMeter().getRegion() +" BUSINESS UNIT");
                    electricityQueryResponse.setErrorCode( "00");
                    electricityQueryResponse.setResponseCode("00");
                    electricityQueryResponse.setResponseDesc(verResponse.getMessage());
                    //verificationDataObj.addProperty("customerType", response.);


                }else{
                    electricityQueryResponse.setErrorCode( "06");
                    electricityQueryResponse.setErrorCode( "06");
                    electricityQueryResponse.setResponseDesc(verResponse.getErr_message());
                }

            } else {

                electricityQueryResponse.setErrorCode( "06");
                electricityQueryResponse.setErrorCode( "06");
                electricityQueryResponse.setResponseDesc("customer details cannot be fetched");
            }

        } catch (SocketTimeoutException ex){
            ex.printStackTrace();
            electricityQueryResponse.setErrorCode( "06");
            electricityQueryResponse.setErrorCode( "06");
            electricityQueryResponse.setResponseDesc("timeout");
        }
        catch (Exception ex) {
            log.error("KANO Verification Error :: ", ex);
            electricityQueryResponse.setErrorCode( "06");
            electricityQueryResponse.setErrorCode( "06");
            electricityQueryResponse.setResponseDesc("failed to verify number");
        }

        log.info("======================= the response from kano: "+gson.toJson(electricityQueryResponse));

        return electricityQueryResponse;
    }

    public ElectricityProcessResponse customPayment(String transactionId, String accountType, String url, String strPaymentRequest, String accessToken){

        ElectricityProcessResponse electricityProcessResponse = new ElectricityProcessResponse();
        try {
            String[] ret = null;
            Gson gson = new Gson();
            double currentArrears = 0;
            double previousArrears = 0;
            String customerArreas = "";
            String kct1 = "";
            String kct2 = "";
            String businessUnit = "";
            Object kctArray = "";



            ret = HttpUtil.sendPost(url, strPaymentRequest, accessToken, "", 3);

            log.info("Response from Kano :: " + ret[1]);
            if (ret[0].equalsIgnoreCase("200")) {

                PaymentResponse paymentResponse = gson.fromJson(ret[1], PaymentResponse.class);

                System.out.println("================= the response object is: "+gson.toJson(paymentResponse));

                if (!paymentResponse.getSuccess()) {
                    electricityProcessResponse.setResponseDesc(paymentResponse.getErr_message());
                    electricityProcessResponse.setResponseCode("06");
                    electricityProcessResponse.setErrorCode("06");

                   return electricityProcessResponse;

                } else {

                    electricityProcessResponse.setResponseDesc(paymentResponse.getMessage());

                    if (paymentResponse.getSuccess()) {
                        String units = String.valueOf(paymentResponse.getPayload().getTransaction().getUnits_count());
                        String tariff_rate = paymentResponse.getPayload().getTransaction().getTariff_rate();
                        Double transactionAmount = paymentResponse.getPayload().getTransaction().getAmount();
                        String vat = String.valueOf(paymentResponse.getPayload().getTransaction().getVat());
                        String debtRemaining = "";
                        String deductions = paymentResponse.getPayload().getTransaction().getDeductions();
                        String externalReference = paymentResponse.getPayload().getTransaction().getTransaction_reference();
                        String feederBand = paymentResponse.getPayload().getTransaction().getService_band();
                        String feederName = paymentResponse.getPayload().getTransaction().getCsp();

                         businessUnit = paymentResponse.getPayload().getTransaction().getRegion()+" BUSINESS UNIT";
                        if(businessUnit == null || businessUnit.isEmpty()) {
                            electricityProcessResponse.setBusinessUnit("KAN BUSINESS UNIT");

                        }else{
                            electricityProcessResponse.setBusinessUnit(businessUnit);
                        }

                        try{
                            currentArrears = paymentResponse.getPayload().getTransaction().getCurrent_arrears();
                        }catch(Exception e){
                            currentArrears = 0;
                            log.info("=======customer current arrears does not exist");
                        }

                        try{
                            previousArrears = paymentResponse.getPayload().getTransaction().getPrevious_arrears();
                        }catch(Exception e){
                            previousArrears = 0;
                            log.info("=======customer current arrears does not exist");
                        }
                        customerArreas = String.valueOf(previousArrears +currentArrears);
                        String fault = "";

                        electricityProcessResponse.setResponseCode(EnumResponseMsg.SUCCESS.responseCode);
                        electricityProcessResponse.setResponseDesc(EnumResponseMsg.SUCCESS.responseMsg);
                        electricityProcessResponse.setFeederName(feederName);
                        electricityProcessResponse.setFeederBand(feederBand);
                        electricityProcessResponse.setTariff(tariff_rate);
                        electricityProcessResponse.setTariffCode(paymentResponse.getPayload().getTransaction().getTariff_code());

                        if (accountType.equalsIgnoreCase("prepaid")) {
                            String token = paymentResponse.getPayload().getTransaction().getTokens().get(0).getCredit();
                            if(paymentResponse.getPayload().getTransaction().getTokens().size() >1) {
                                kct1 = paymentResponse.getPayload().getTransaction().getTokens().get(1).getKct_1();
                                kct2 = paymentResponse.getPayload().getTransaction().getTokens().get(2).getKct_2();
                            }

                            String costOfUnit = String.valueOf(paymentResponse.getPayload().getTransaction().getCost_of_units());
                            MainTokenData tokenData = new MainTokenData();
                            tokenData.setUnit(units);
                            tokenData.setAmount(String.valueOf(transactionAmount));
                            tokenData.setVat(vat);
                            tokenData.setFixedCharge(costOfUnit);
                            tokenData.setToken(token);

                            electricityProcessResponse.setBusinessUnit(businessUnit);

                            electricityProcessResponse.setMainToken(tokenData);
                            electricityProcessResponse.setResponseDesc("success");
                            electricityProcessResponse.setExternalReference(externalReference);
                            //    paymentDataObj.addProperty("message", paymentResponse.getMessage());
                            kct1 = paymentResponse.getPayload().getTransaction().getTokens().get(1).getKct_1();
                            kct2 = paymentResponse.getPayload().getTransaction().getTokens().get(2).getKct_2();

                            JsonObject keyChangeToken = new JsonObject();
                            if(!kct1.isEmpty()) {
                                System.out.println("===============key change token exist");
                                keyChangeToken.addProperty("kct1",kct1);
                                keyChangeToken.addProperty("kct2",kct2);

                            }

                            electricityProcessResponse.setResponseCode(EnumResponseMsg.SUCCESS.responseCode);
                            electricityProcessResponse.setResponseDesc(EnumResponseMsg.SUCCESS.responseMsg);
                            electricityProcessResponse.setCustomerName(paymentResponse.getPayload().getTransaction().getCustomer_name());
                            electricityProcessResponse.setAmount(String.valueOf(paymentResponse.getPayload().getTransaction().getAmount()));
                            electricityProcessResponse.setUnitsPurchased(costOfUnit);
                            electricityProcessResponse.setUnitsPayment(String.valueOf(paymentResponse.getPayload().getTransaction().getUnits_count()));
                            electricityProcessResponse.setAccountNumber(String.valueOf(paymentResponse.getPayload().getTransaction().getAccount_number()));
                            electricityProcessResponse.setAmount(paymentResponse.getPayload().getTransaction().getAccount_type());
                            //electricityProcessResponse.setResponseCode("transaction_status", paymentResponse.getPayload().getTransaction().getTransaction_status());
                            electricityProcessResponse.setTariffCode(paymentResponse.getPayload().getTransaction().getTariff_rate());
                            electricityProcessResponse.setTariffCode(paymentResponse.getPayload().getTransaction().getTariff_code());
                            electricityProcessResponse.setVat(vat);
                            electricityProcessResponse.setCustomerArrears(customerArreas);
                            electricityProcessResponse.setDeductions(deductions);
                            electricityProcessResponse.setResponseCode("00");

                            //paymentDataObj.addProperty("costOfUnit", costOfUnit);
                            fault = paymentResponse.getPayload().getTransaction().getAccount_number() + "," + token + "," + units + "," + tariff_rate + "," + vat + "," + debtRemaining;
                            //paymentDataObj.addProperty("fault",fault);

                        }else {
                            vat = String.valueOf(paymentResponse.getPayload().getTransaction().getVat());

                            electricityProcessResponse.setResponseCode(EnumResponseMsg.SUCCESS.responseCode);
                            electricityProcessResponse.setResponseDesc(EnumResponseMsg.SUCCESS.responseMsg);
                            electricityProcessResponse.setExternalReference(externalReference);
                            //paymentDataObj.addProperty("id", paymentResponse.getPayload().getTransaction().getId());
                            electricityProcessResponse.setCustomerName(paymentResponse.getPayload().getTransaction().getCustomer_name());
                            electricityProcessResponse.setAmount(String.valueOf(transactionAmount));
                            electricityProcessResponse.setUnitsPurchased(String.valueOf(paymentResponse.getPayload().getTransaction().getUnits_count()));
                            electricityProcessResponse.setUnitsPayment(String.valueOf(paymentResponse.getPayload().getTransaction().getUnits_count()));
                            electricityProcessResponse.setAccountNumber(String.valueOf(paymentResponse.getPayload().getTransaction().getAccount_number()));
                            electricityProcessResponse.setAccountType(paymentResponse.getPayload().getTransaction().getAccount_type());
                            //paymentDataObj.addProperty("transaction_status", paymentResponse.getPayload().getTransaction().getTransaction_status());
                            electricityProcessResponse.setPayment_method(paymentResponse.getPayload().getTransaction().getPayment_method());
                            electricityProcessResponse.setTariff(paymentResponse.getPayload().getTransaction().getTariff_rate());
                            electricityProcessResponse.setTariffCode(paymentResponse.getPayload().getTransaction().getTariff_code());
                            electricityProcessResponse.setVat(String.valueOf(paymentResponse.getPayload().getTransaction().getVat()));
                            electricityProcessResponse.setCustomerArrears(customerArreas);
                            electricityProcessResponse.setResponseCode("00");
                            electricityProcessResponse.setTransaction_date(paymentResponse.getPayload().getTransaction().getTransaction_date().toString());

                            fault = paymentResponse.getPayload().getTransaction().getAccount_number() + "," + units + "," + tariff_rate + "," + vat + "," + customerArreas;

                           // paymentDataObj.addProperty("fault", fault);
                        }
                    }

                }

            }else if(ret[0].equalsIgnoreCase("408")){
                electricityProcessResponse.setResponseCode(EnumResponseMsg.TIMEOUT.responseCode);
                electricityProcessResponse.setResponseDesc(EnumResponseMsg.TIMEOUT.responseMsg);

            }
            else {
                log.info(transactionId +" TRANSACTION POSTING FAILED WITH RESPONSE CODE :: "+ret[0]);
                electricityProcessResponse.setResponseCode(ret[0]);
                electricityProcessResponse.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);

            }
        } catch (SocketTimeoutException ex){
            ex.printStackTrace();
            electricityProcessResponse.setResponseCode(EnumResponseMsg.TIMEOUT.responseCode);
            electricityProcessResponse.setResponseDesc(EnumResponseMsg.TIMEOUT.responseMsg);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            log.error("KANO Payment Error :: ", ex);
            System.out.println("========= exception: "+ex);
            electricityProcessResponse.setResponseCode(EnumResponseMsg.TIMEOUT.responseCode);
            electricityProcessResponse.setResponseDesc(EnumResponseMsg.TIMEOUT.responseMsg);

        }
        return electricityProcessResponse;
    }
}
