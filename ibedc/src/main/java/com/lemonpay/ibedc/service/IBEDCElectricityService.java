package com.lemonpay.ibedc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.lemonpay.ibedc.Action.EnumResponseMsg;
import com.lemonpay.ibedc.dto.response.*;
import com.lemonpay.lemonpayvas.electricity.phcnnode.PHCNNode;
import com.lemonpay.lemonpayvas.electricity.phcnnode.dto.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class IBEDCElectricityService extends PHCNNode {

    @Value("${serviceId}")
    private String serviceId;
    @Value("${api-key}")
    private String apiKey;
    @Value("${secret-key}")
    private String secretKey;
    @Value("${public-key}")
    private String publicKey;
    @Value("${baseUrl}")
    private String baseUrl;
    @Value("${verify-endpoint}")
    private String verifyEndpoint;
    private String postpaidUrl;
    private String paymentEnpoint;

    IBEDCService ibedcService = new IBEDCService();

    @Override
    public ElectricityQueryResponse query(ElectricityQueryRequest electricityQueryRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ElectricityQueryResponse response = new ElectricityQueryResponse();
        try {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>the request sent for IBEDC electricity: "+objectMapper.writeValueAsString(electricityQueryRequest));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if(electricityQueryRequest.getType().equalsIgnoreCase("2")){
            response =  postPaidCustomerInfo(electricityQueryRequest);
        }else if(electricityQueryRequest.getType().equalsIgnoreCase("1")){
            response =  prepaidInfoPosting(electricityQueryRequest);
        }
        return response;
    }

    private ElectricityQueryResponse prepaidInfoPosting(ElectricityQueryRequest electricityQueryRequest) {

        String tariffRate = null;
        String result = "";

        JsonObject verificationDataObj = new JsonObject();
        ElectricityQueryResponse response = new ElectricityQueryResponse();

        String accountOrMeterNo = electricityQueryRequest.getPayerId();


        try {

            QueryResponse queryResponse = ibedcService.customerQuery(accountOrMeterNo, "prepaid", baseUrl, verifyEndpoint, apiKey, publicKey);
            if (queryResponse != null){
                log.info("Pre-paid customer query :: ");
                if (queryResponse.getCode().equalsIgnoreCase("000")){

                    verificationDataObj.addProperty("requestType", "verification");
                    verificationDataObj.addProperty("disco", "IBEDC");
                    verificationDataObj.addProperty("accountType", "prepaid");
                    verificationDataObj.addProperty("accountNumber", accountOrMeterNo);
                    verificationDataObj.addProperty("uniqueTransId", electricityQueryRequest.getReference());
                    verificationDataObj.addProperty("accountNumber", electricityQueryRequest.getPayerId());

                    response.setRequestType("verification");
                    response.setDisco("IBEDC");
                    response.setUniqueTransId(electricityQueryRequest.getReference());
                    response.setAccountNumber(electricityQueryRequest.getPayerId());

                    ResponseContent responseContent = queryResponse.getContent();

                    if (queryResponse.getCode().equalsIgnoreCase("000")){
                        verificationDataObj.addProperty("customerName", responseContent.getCustomer_Name());
                        verificationDataObj.addProperty("customerAddress", responseContent.getAddress());

                        verificationDataObj.addProperty("businessUnit", EnumResponseMsg.business_unit);
                        verificationDataObj.addProperty("state", "");
                        verificationDataObj.addProperty("errorCode", queryResponse.getCode());
                        verificationDataObj.addProperty("responseCode", "000");
                        verificationDataObj.addProperty("responseDesc", EnumResponseMsg.SUCCESS.toString());
                        verificationDataObj.addProperty("customerArrears", responseContent.getCustomer_Arrears());
                        verificationDataObj.addProperty("accountType", responseContent.getMeter_Type());
                        verificationDataObj.addProperty("canVend", responseContent.getCan_Vend());
                        verificationDataObj.addProperty("minPurchaseAmount", responseContent.getMin_Purchase_Amount());
                        verificationDataObj.addProperty("meterNumber", responseContent.getMeterNumber());
                        verificationDataObj.addProperty("Service_Band", responseContent.getService_Band());
                        verificationDataObj.addProperty("Meter_Type", responseContent.getMeter_Type());
                        verificationDataObj.addProperty("WrongBillersCode", responseContent.getWrongBillersCode());

                        Commission_details commission = responseContent.getCommission_details();
                        if (commission != null) {
                            verificationDataObj.addProperty("amount", commission.getAmount());
                            verificationDataObj.addProperty("rate", commission.getRate());
                            verificationDataObj.addProperty("rate_type", commission.getRate_type());
                            verificationDataObj.addProperty("computation_type", commission.getComputation_type());
                        }

                        response.setState("");
                        response.setErrorCode("");
                        response.setResponseCode(queryResponse.getCode());
                        response.setResponseDesc(EnumResponseMsg.SUCCESS.toString());
                        response.setCustomerName(responseContent.getCustomer_Name());
                        response.setCustomerAddress(responseContent.getAddress());
                        response.setBusinessUnit(EnumResponseMsg.business_unit);
                        response.setAccountNumber(responseContent.getMeterNumber());
                        response.setDisco("IBEDC");
                        response.setCustomerType(responseContent.getMeter_Type());
                        //response.setVend
                        String serviceBand = responseContent.getService_Band();
                        String minPurchaseAmount;

                        if ("A".equalsIgnoreCase(serviceBand)) {
                            minPurchaseAmount = "5000";
                        } else {
                            minPurchaseAmount = "2000";
                        }

                        response.setMinimumPurchase(minPurchaseAmount);
                        verificationDataObj.addProperty("minPurchaseAmount", minPurchaseAmount);

                        response.setAccountNumber(responseContent.getMeterNumber());
                        //response.setServiceBand
                        //response.setWrongBillersCode
                        if (commission != null) {
                            response.setTariff(commission.getRate());
                            response.setTariffClass(commission.getRate_type());
                        }
                        //response.setComputationType

                    }else {
                        verificationDataObj.addProperty("errorCode", queryResponse.getCode());
                        verificationDataObj.addProperty("responseCode", "56");
                        verificationDataObj.addProperty("responseDesc", "Customer query failed");

                        response.setErrorCode(queryResponse.getCode());
                        response.setResponseCode("56");
                        response.setResponseDesc("Customer query failed");

                    }
                }else {
                    log.info("FAILED RESPONSE FROM DISCO");
                    verificationDataObj.addProperty("errorCode", EnumResponseMsg.DISCOERROR.responseCode);
                    verificationDataObj.addProperty("responseCode", EnumResponseMsg.DISCOERROR.responseCode);
                    verificationDataObj.addProperty("responseDesc", EnumResponseMsg.DISCOERROR.responseMsg);

                    response.setErrorCode(EnumResponseMsg.DISCOERROR.responseCode);
                    response.setResponseCode(EnumResponseMsg.DISCOERROR.responseCode);
                    response.setResponseDesc(EnumResponseMsg.DISCOERROR.responseMsg);

                }

            } else {
                log.info("FAILED RESPONSE FROM DISCO");
                verificationDataObj.addProperty("errorCode", EnumResponseMsg.DISCOERROR.responseCode);
                verificationDataObj.addProperty("responseCode", EnumResponseMsg.DISCOERROR.responseCode);
                verificationDataObj.addProperty("responseDesc", EnumResponseMsg.DISCOERROR.responseMsg);

                response.setErrorCode(EnumResponseMsg.DISCOERROR.responseCode);
                response.setResponseCode(EnumResponseMsg.DISCOERROR.responseCode);
                response.setResponseDesc(EnumResponseMsg.DISCOERROR.responseMsg);

            }

        }catch (Exception e) {
            log.info("::::::::::::::the Exception  CustomerInfo Failure" + e);
            verificationDataObj.addProperty("errorCode", EnumResponseMsg.FAILED.responseCode);
            verificationDataObj.addProperty("responseCode", EnumResponseMsg.TIMEOUT.responseCode);
            verificationDataObj.addProperty("responseDesc", EnumResponseMsg.FAILED.responseMsg);

            response.setErrorCode(EnumResponseMsg.FAILED.responseCode);
            response.setResponseCode(EnumResponseMsg.TIMEOUT.responseCode);
            response.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);

            log.info(" CUSTOMER QUERY RESPONSE " + verificationDataObj.toString());
        }
        result = verificationDataObj.toString();
        log.info(" CUSTOMER QUERY RESPONSE:::::" + response);
        return response;
    }

    private ElectricityQueryResponse postPaidCustomerInfo(ElectricityQueryRequest electricityQueryRequest) {
        String tariffRate = null;
        String result = "";

        JsonObject verificationDataObj = new JsonObject();
        ElectricityQueryResponse response = new ElectricityQueryResponse();

        String accountOrMeterNo = electricityQueryRequest.getPayerId();
        String action = "postpaid";


        try {

            QueryResponse queryResponse = ibedcService.customerQuery(accountOrMeterNo, action, baseUrl, verifyEndpoint, apiKey, publicKey);
            if (queryResponse != null){
                log.info("Pre-paid customer query :: ");
                if (queryResponse.getCode().equalsIgnoreCase("000")){

                    verificationDataObj.addProperty("requestType", "verification");
                    verificationDataObj.addProperty("disco", "IBEDC");
                    verificationDataObj.addProperty("accountType", "prepaid");
                    verificationDataObj.addProperty("accountNumber", accountOrMeterNo);
                    verificationDataObj.addProperty("uniqueTransId", electricityQueryRequest.getReference());
                    verificationDataObj.addProperty("accountNumber", electricityQueryRequest.getPayerId());

                    response.setRequestType("verification");
                    response.setDisco("IBEDC");
                    response.setUniqueTransId(electricityQueryRequest.getReference());
                    response.setAccountNumber(electricityQueryRequest.getPayerId());

                    ResponseContent responseContent = queryResponse.getContent();

                    if (queryResponse.getCode().equalsIgnoreCase("000")){
                        verificationDataObj.addProperty("customerName", responseContent.getCustomer_Name());
                        verificationDataObj.addProperty("customerAddress", responseContent.getAddress());

                        verificationDataObj.addProperty("businessUnit", EnumResponseMsg.business_unit);
                        verificationDataObj.addProperty("state", "");
                        verificationDataObj.addProperty("errorCode", queryResponse.getCode());
                        verificationDataObj.addProperty("responseCode", "000");
                        verificationDataObj.addProperty("responseDesc", EnumResponseMsg.SUCCESS.toString());
                        verificationDataObj.addProperty("customerArrears", responseContent.getCustomer_Arrears());
                        verificationDataObj.addProperty("accountType", responseContent.getMeter_Type());
                        verificationDataObj.addProperty("canVend", responseContent.getCan_Vend());
                        verificationDataObj.addProperty("minPurchaseAmount", responseContent.getMin_Purchase_Amount());
                        verificationDataObj.addProperty("meterNumber", responseContent.getMeterNumber());
                        verificationDataObj.addProperty("Service_Band", responseContent.getService_Band());
                        verificationDataObj.addProperty("Meter_Type", responseContent.getMeter_Type());
                        verificationDataObj.addProperty("WrongBillersCode", responseContent.getWrongBillersCode());

                        Commission_details commission = responseContent.getCommission_details();
                        if (commission != null) {
                            verificationDataObj.addProperty("amount", commission.getAmount());
                            verificationDataObj.addProperty("rate", commission.getRate());
                            verificationDataObj.addProperty("rate_type", commission.getRate_type());
                            verificationDataObj.addProperty("computation_type", commission.getComputation_type());
                        }

                        response.setState("");
                        response.setErrorCode("");
                        response.setResponseCode(queryResponse.getCode());
                        response.setResponseDesc(EnumResponseMsg.SUCCESS.toString());
                        response.setCustomerName(responseContent.getCustomer_Name());
                        response.setCustomerAddress(responseContent.getAddress());
                        response.setBusinessUnit(EnumResponseMsg.business_unit);
                        response.setAccountNumber(responseContent.getMeterNumber());
                        response.setDisco("IBEDC");
                        response.setCustomerType(responseContent.getMeter_Type());
                        //response.setVend
                        String serviceBand = responseContent.getService_Band();
                        String minPurchaseAmount;

                        if ("A".equalsIgnoreCase(serviceBand)) {
                            minPurchaseAmount = "5000";
                        } else {
                            minPurchaseAmount = "2000";
                        }

                        response.setMinimumPurchase(minPurchaseAmount);
                        verificationDataObj.addProperty("minPurchaseAmount", minPurchaseAmount);

                        response.setAccountNumber(responseContent.getMeterNumber());
                        //response.setServiceBand
                        //response.setWrongBillersCode
                        if (commission != null) {
                            response.setTariff(commission.getRate());
                            response.setTariffClass(commission.getRate_type());
                        }
                        //response.setComputationType

                    }else {
                        verificationDataObj.addProperty("errorCode", queryResponse.getCode());
                        verificationDataObj.addProperty("responseCode", "56");
                        verificationDataObj.addProperty("responseDesc", "Customer query failed");

                        response.setErrorCode(queryResponse.getCode());
                        response.setResponseCode("56");
                        response.setResponseDesc("Customer query failed");

                    }
                }else {
                    log.info("FAILED RESPONSE FROM DISCO");
                    verificationDataObj.addProperty("errorCode", EnumResponseMsg.DISCOERROR.responseCode);
                    verificationDataObj.addProperty("responseCode", EnumResponseMsg.DISCOERROR.responseCode);
                    verificationDataObj.addProperty("responseDesc", EnumResponseMsg.DISCOERROR.responseMsg);

                    response.setErrorCode(EnumResponseMsg.DISCOERROR.responseCode);
                    response.setResponseCode(EnumResponseMsg.DISCOERROR.responseCode);
                    response.setResponseDesc(EnumResponseMsg.DISCOERROR.responseMsg);

                }

            } else {
                log.info("FAILED RESPONSE FROM DISCO");
                verificationDataObj.addProperty("errorCode", EnumResponseMsg.DISCOERROR.responseCode);
                verificationDataObj.addProperty("responseCode", EnumResponseMsg.DISCOERROR.responseCode);
                verificationDataObj.addProperty("responseDesc", EnumResponseMsg.DISCOERROR.responseMsg);

                response.setErrorCode(EnumResponseMsg.DISCOERROR.responseCode);
                response.setResponseCode(EnumResponseMsg.DISCOERROR.responseCode);
                response.setResponseDesc(EnumResponseMsg.DISCOERROR.responseMsg);

            }

        }catch (Exception e) {
            log.info("::::::::::::::the Exception  CustomerInfo Failure" + e);
            verificationDataObj.addProperty("errorCode", EnumResponseMsg.FAILED.responseCode);
            verificationDataObj.addProperty("responseCode", EnumResponseMsg.TIMEOUT.responseCode);
            verificationDataObj.addProperty("responseDesc", EnumResponseMsg.FAILED.responseMsg);

            response.setErrorCode(EnumResponseMsg.FAILED.responseCode);
            response.setResponseCode(EnumResponseMsg.TIMEOUT.responseCode);
            response.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);

            log.info(" CUSTOMER QUERY RESPONSE " + verificationDataObj.toString());
        }
        result = verificationDataObj.toString();
        log.info(" CUSTOMER QUERY RESPONSE:::::" + response);
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

    private ElectricityProcessResponse doPostPaidTransactionPosting(ElectricityProcessRequest electricityProcessRequest) {
        JsonObject paymentDataObj = new JsonObject();
        ElectricityProcessResponse response = new ElectricityProcessResponse();

        String uniqueTransId = electricityProcessRequest.getReference();
        String paymentChannel = electricityProcessRequest.getPaymentChannel();
        String mobile = electricityProcessRequest.getMobile();
        String channelCode = electricityProcessRequest.getReference().substring(0, 3);
        String meterType = electricityProcessRequest.getType();
        String amount = String.valueOf(electricityProcessRequest.getAmount());
        String accountOrMeterNo = electricityProcessRequest.getPayerId();
        String request_id = electricityProcessRequest.getReference();
        String action = "postpaid";

        String units = null;
        String tariffRate = null;
        String vat = null;
        String token = null;

        log.info( "::::::::::::::::::::::::::::" + action + "TRANSACTION POSTING REQUEST::::" + electricityProcessRequest);

        paymentDataObj.addProperty("requestType", "payment");
        paymentDataObj.addProperty("disco", "EKO");
        paymentDataObj.addProperty("accountType", "postpaid");
        paymentDataObj.addProperty("uniqueTransId", uniqueTransId);
        paymentDataObj.addProperty("accountNumber", accountOrMeterNo);

        try {

            if (mobile.length() == 13) {
                mobile = "0" + mobile.substring(3);
            } else if (mobile.length() == 11) {
                mobile = "0" + mobile.substring(1);
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

            paymentDataObj.addProperty("mobileNo", mobile);
            paymentDataObj.addProperty("tariff", tariffRate);

            response.setRequestType("payment");
            response.setDisco("IBEDC");
         //   response.setAccountType("");
            response.setUniqueTransId(uniqueTransId);

            log.info(":::::::::::::::::::::::::calling " + action + " custom payment");

            PaymentResponse paymentResponse = ibedcService.customPayment(
                    request_id, serviceId, accountOrMeterNo, action,
                    Double.valueOf(amount), mobile, baseUrl, paymentEnpoint, secretKey
            );

            if (!"000".equals(paymentResponse.getCode())) {
                log.info(":::::::::::::::::::::::::::::the verification is unsuccessful");

                paymentDataObj.addProperty("errorCode", paymentResponse.getCode());
                paymentDataObj.addProperty("responseCode", "06");

                response.setErrorCode(paymentResponse.getCode());
                response.setResponseCode("06");
                return response;
            }

            PaymentContent content = paymentResponse.getContent();
            PaymentResponse payResponse = new PaymentResponse();

            units = paymentResponse.getUnits();
            tariffRate = paymentResponse.getTariff();
            vat = paymentResponse.getTax();
            token = paymentResponse.getToken();
            String kct1 = "";
            String kct2 = "";

            if (paymentResponse.getKCT1() != null) {
                kct1 = paymentResponse.getKCT1();
            }

            if (paymentResponse.getKCT2() != null) {
                kct2 = paymentResponse.getKCT2();
            }

            String combinedKctTokens;
            if (!kct1.isEmpty() && !kct2.isEmpty()) {
                combinedKctTokens = kct1 + "," + kct2;
            } else {
                combinedKctTokens = kct1 + kct2;
            }


            MainTokenData tokenData = new MainTokenData();
            tokenData.setUnit(units);
            tokenData.setAmount(amount);
            tokenData.setVat(vat);
            tokenData.setFixedCharge(tariffRate);
            tokenData.setToken(token);


            tokenData.setKctTokens(combinedKctTokens);
            // tokenData.setKctTokens(kct1,kct2);
            response.setMainToken(tokenData);

            paymentDataObj.addProperty("responseCode", "00");
            paymentDataObj.addProperty("responseDesc", "Successful");
            paymentDataObj.addProperty("businessUnit", "IBEDC BUSINESS UNIT");
            paymentDataObj.addProperty("accountOrMeterNo", accountOrMeterNo);
            paymentDataObj.addProperty("", paymentResponse.getCustomerAddress());
            paymentDataObj.addProperty("businessUnit", "");
            paymentDataObj.addProperty("tariffCode", "");
            paymentDataObj.addProperty("customerArrears", "");
            paymentDataObj.addProperty("undertaking", "");

            response.setResponseCode("00");
            response.setResponseDesc("Successful");
            response.setBusinessUnit("IBEDC BUSINESS UNIT");
            response.setAccountNumber(accountOrMeterNo);
            response.setAccountType(content.getTransactions().getType());
            response.setCustomerAddress(paymentResponse.getCustomerAddress());
            response.setTariff(paymentResponse.getTariff());
            response.setBusinessUnit("");
            response.setDisco("IBEDC");
            //response.setBusinessUnit();

        } catch (SocketTimeoutException e) {
            log.info("::::::::::::::::::::::::::Time out for " + action + ": " + e);

            paymentDataObj.addProperty("errorCode", EnumResponseMsg.TIMEOUT.responseCode);
            paymentDataObj.addProperty("responseCode", EnumResponseMsg.TIMEOUT.responseCode);
            paymentDataObj.addProperty("responseDesc", EnumResponseMsg.TIMEOUT.responseMsg);

            response.setErrorCode(EnumResponseMsg.TIMEOUT.responseCode);
            response.setResponseCode(EnumResponseMsg.TIMEOUT.responseCode);
            response.setResponseDesc(EnumResponseMsg.TIMEOUT.responseMsg);

        } catch (Exception e) {
            log.info(":::::::::::::::::::::::the Exception CustomerInfo Failure: " + e);

            paymentDataObj.addProperty("errorCode", EnumResponseMsg.FAILED.responseCode);
            paymentDataObj.addProperty("responseCode", EnumResponseMsg.FAILED.responseCode);
            paymentDataObj.addProperty("responseDesc", EnumResponseMsg.FAILED.responseMsg);

            response.setErrorCode(EnumResponseMsg.FAILED.responseCode);
            response.setResponseCode(EnumResponseMsg.FAILED.responseCode);
            response.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);
        }

        log.info("::::::::::::::::::::::::::::::" + action + " RESPONSE for ibedc: " + paymentDataObj.toString());
        return response;
    }

    private ElectricityProcessResponse doPrepaidTransactionPosting(ElectricityProcessRequest electricityProcessRequest) {
        JsonObject paymentDataObj = new JsonObject();
        ElectricityProcessResponse response = new ElectricityProcessResponse();

        String uniqueTransId = electricityProcessRequest.getReference();
        String paymentChannel = electricityProcessRequest.getPaymentChannel();
        String mobile = electricityProcessRequest.getMobile();
        String channelCode = electricityProcessRequest.getReference().substring(0, 3);
        String meterType = electricityProcessRequest.getType();
        String amount = String.valueOf(electricityProcessRequest.getAmount());
        String accountOrMeterNo = electricityProcessRequest.getPayerId();
        String request_id = electricityProcessRequest.getReference();
        String action = "prepaid";

        String units = null;
        String tariffRate = null;
        String vat = null;
        String token = null;

        log.info(":::::::::::::::::::::::::::: PREPAID TRANSACTION POSTING REQUEST::::" + electricityProcessRequest);

        paymentDataObj.addProperty("requestType", "payment");
        paymentDataObj.addProperty("disco", "EKO");
        paymentDataObj.addProperty("accountType", "prepaid");
        paymentDataObj.addProperty("uniqueTransId", uniqueTransId);
        paymentDataObj.addProperty("accountNumber", accountOrMeterNo);

        try {

            if (mobile.length() == 13) {
                mobile = "0" + mobile.substring(3);
            } else if (mobile.length() == 11) {
                mobile = "0" + mobile.substring(1);
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

            paymentDataObj.addProperty("mobileNo", mobile);
            paymentDataObj.addProperty("tarfii", tariffRate);

            response.setRequestType("payment");
            response.setDisco("IBEDC");
            response.setAccountType("prepaid");
            response.setUniqueTransId(uniqueTransId);

            log.info(":::::::::::::::::::::::::calling IBEDC prepaid custom payment");

            PaymentResponse paymentResponse = ibedcService.customPayment(
                    request_id, serviceId, accountOrMeterNo, action,
                    Double.valueOf(amount), mobile, baseUrl, paymentEnpoint, secretKey
            );

            if (!"000".equals(paymentResponse.getCode())) {
                log.info(":::::::::::::::::::::::::::::the verification is unsuccessful");

                paymentDataObj.addProperty("errorCode", paymentResponse.getCode());
                paymentDataObj.addProperty("responseCode", "06");

                response.setErrorCode(paymentResponse.getCode());
                response.setResponseCode("06");
                return response;
            }

            PaymentContent content = paymentResponse.getContent();
            PaymentResponse payResponse = new PaymentResponse();

            units = paymentResponse.getUnits();
            tariffRate = paymentResponse.getTariff();
            vat = paymentResponse.getTax();
            token = paymentResponse.getToken();
            String kct1 = "";
            String kct2 = "";

            if (paymentResponse.getKCT1() != null) {
                kct1 = paymentResponse.getKCT1();
            }

            if (paymentResponse.getKCT2() != null) {
                kct2 = paymentResponse.getKCT2();
            }

            String combinedKctTokens;
            if (!kct1.isEmpty() && !kct2.isEmpty()) {
                combinedKctTokens = kct1 + "," + kct2;
            } else {
                combinedKctTokens = kct1 + kct2;
            }


            MainTokenData tokenData = new MainTokenData();
            tokenData.setUnit(units);
            tokenData.setAmount(amount);
            tokenData.setVat(vat);
            tokenData.setFixedCharge(tariffRate);
            tokenData.setToken(token);


            tokenData.setKctTokens(combinedKctTokens);
           // tokenData.setKctTokens(kct1,kct2);
            response.setMainToken(tokenData);

            paymentDataObj.addProperty("responseCode", "00");
            paymentDataObj.addProperty("responseDesc", "Successful");
            paymentDataObj.addProperty("businessUnit", "IBEDC BUSINESS UNIT");
            paymentDataObj.addProperty("accountOrMeterNo", accountOrMeterNo);
            paymentDataObj.addProperty("tariffCode", "");
            paymentDataObj.addProperty("customerArrears", "");
            paymentDataObj.addProperty("undertaking", "");


            response.setResponseCode("00");
            response.setResponseDesc("Successful");
            response.setBusinessUnit("IBEDC BUSINESS UNIT");
            response.setAccountNumber(accountOrMeterNo);
            response.setAccountType(content.getTransactions().getType());
            response.setCustomerAddress(paymentResponse.getCustomerAddress());
            response.setTariff(paymentResponse.getTariff());
            response.setDisco("IBEDC");
            response.setBusinessUnit("IBEDC BUSINESS UNIT");

        } catch (SocketTimeoutException e) {
            log.info("::::::::::::::::::::::::::Time out for " + action + ": " + e);

            paymentDataObj.addProperty("errorCode", EnumResponseMsg.TIMEOUT.responseCode);
            paymentDataObj.addProperty("responseCode", EnumResponseMsg.TIMEOUT.responseCode);
            paymentDataObj.addProperty("responseDesc", EnumResponseMsg.TIMEOUT.responseMsg);

            response.setErrorCode(EnumResponseMsg.TIMEOUT.responseCode);
            response.setResponseCode(EnumResponseMsg.TIMEOUT.responseCode);
            response.setResponseDesc(EnumResponseMsg.TIMEOUT.responseMsg);

        } catch (Exception e) {
            log.info(":::::::::::::::::::::::the Exception CustomerInfo Failure: " + e);

            paymentDataObj.addProperty("errorCode", EnumResponseMsg.FAILED.responseCode);
            paymentDataObj.addProperty("responseCode", EnumResponseMsg.FAILED.responseCode);
            paymentDataObj.addProperty("responseDesc", EnumResponseMsg.FAILED.responseMsg);

            response.setErrorCode(EnumResponseMsg.FAILED.responseCode);
            response.setResponseCode(EnumResponseMsg.FAILED.responseCode);
            response.setResponseDesc(EnumResponseMsg.FAILED.responseMsg);
        }

        log.info("::::::::::::::::::::::::::::::" + action + " RESPONSE for ibedc: " + paymentDataObj.toString());
        return response;
    }


    @Override
    public ElectricityProcessResponse reQuery(ElectricityReQueryRequest electricityReQueryRequest) {
        return null;
    }

    @Override
    public PingResponse ping(ElectricityQueryRequest electricityQueryRequest) {
        return null;
    }
}
