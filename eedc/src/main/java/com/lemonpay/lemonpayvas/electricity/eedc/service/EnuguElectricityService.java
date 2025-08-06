package com.lemonpay.lemonpayvas.electricity.eedc.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lemonpay.lemonpayvas.electricity.eedc.enums.Response;
import com.lemonpay.lemonpayvas.electricity.eedc.requestdto.ProcessRequestdto;
import com.lemonpay.lemonpayvas.electricity.eedc.requestdto.QueryRequest;
import com.lemonpay.lemonpayvas.electricity.eedc.requestdto.RequeryRequest;
import com.lemonpay.lemonpayvas.electricity.eedc.responsedto.EnuguElectricityQueryResponse;
import com.lemonpay.lemonpayvas.electricity.eedc.responsedto.ProcessResponse;
import com.lemonpay.lemonpayvas.electricity.eedc.util.GenerateRequestId;
import com.lemonpay.lemonpayvas.electricity.eedc.util.HTTPUtils;
import com.lemonpay.lemonpayvas.electricity.phcnnode.PHCNNode;
import com.lemonpay.lemonpayvas.electricity.phcnnode.dto.*;

import com.lemonpay.lemonpayvas.electricity.redisutility.redisservice.LemonpayRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Component("phcnenu")
public class EnuguElectricityService extends PHCNNode {


    @Value("${serviceId}")
    private String serviceId;
    @Value("${api-key}")
    private String apiKey;
    @Value("${secret-key}")
    private String secretKey;
    @Value("${baseurl}")
    private String baseUrl;
    @Value("${pay}")
    private String pay;
    @Value("${query}")
    private String query;
    @Value("${requery}")
    private String requery;

    @Autowired
    private LemonpayRedisService lemonpayRedisService;

    @Override
    public ElectricityQueryResponse query(ElectricityQueryRequest electricityQueryRequest) {


        Gson gson = new Gson();
        String type = null;
        log.info("============================= The electricity query request is: "+gson.toJson(electricityQueryRequest));

        QueryRequest queryRequest = new QueryRequest();

        ElectricityQueryResponse electricityQueryResponse = null;
        if(electricityQueryRequest.getPayerId() == null || electricityQueryRequest.getPayerId().isEmpty()){
            electricityQueryResponse = new ElectricityQueryResponse();
            electricityQueryResponse.setResponseCode(Response.INVALID_ACCOUNT_NUMBER.code);
            electricityQueryResponse.setResponseDesc(Response.INVALID_ACCOUNT_NUMBER.toString());

            gson.toJson("===================== the response from query is: "+gson.toJson(electricityQueryResponse));

            return electricityQueryResponse;
        }

        if(electricityQueryRequest.getType() == null && (!electricityQueryRequest.getType().equalsIgnoreCase("1") && !electricityQueryRequest.getType().equalsIgnoreCase("2") )){
            electricityQueryResponse = new ElectricityQueryResponse();
            electricityQueryResponse.setResponseCode(Response.INVALID_ACCOUNT_METER_TYPE.code);
            electricityQueryResponse.setResponseDesc(Response.INVALID_ACCOUNT_METER_TYPE.toString());

            gson.toJson("===================== the response from query is: "+gson.toJson(electricityQueryResponse));

            return electricityQueryResponse;
        }

        return verify( electricityQueryRequest);


    }

    @Override
    public ElectricityProcessResponse process(ElectricityProcessRequest electricityProcessRequest) {
        Gson gson = new Gson();
        log.info("=============================== the response for process is: "+gson.toJson(electricityProcessRequest));

        // Define the format: YYYYMMDDHHmm (note: 'mm' is for minute)
        String url = baseUrl + pay;
        ProcessRequestdto processRequestdto = new ProcessRequestdto();
        processRequestdto.setAmount(electricityProcessRequest.getAmount());
        String request_id = GenerateRequestId.getRequestId();
        processRequestdto.setRequest_id(request_id);
        processRequestdto.setBillersCode(electricityProcessRequest.getPayerId());

        processRequestdto.setPhone(Long.parseLong(electricityProcessRequest.getMobile()));
        processRequestdto.setServiceID(serviceId);
        String accoutType = null;

        if(electricityProcessRequest.getType().equalsIgnoreCase("1")){
            accoutType = "prepaid";
            processRequestdto.setVariation_code("prepaid");
        }else if(electricityProcessRequest.getType().equalsIgnoreCase("2")){
            accoutType = "postpaid";
            processRequestdto.setVariation_code("postpaid");
        }else{
            ElectricityProcessResponse electricityProcessResponse = new ElectricityProcessResponse();
            electricityProcessResponse.setResponseDesc(Response.INVALID_ACCOUNT_METER_TYPE.toString());
            electricityProcessResponse.setResponseCode(Response.INVALID_ACCOUNT_METER_TYPE.code);

            return electricityProcessResponse;
        }

        if(electricityProcessRequest.getAmount() < 1){
            accoutType = "prepaid";

            ElectricityProcessResponse electricityProcessResponse = new ElectricityProcessResponse();
            electricityProcessResponse.setResponseDesc(Response.INVALID_AMOUNT.toString());
            electricityProcessResponse.setResponseCode(Response.INVALID_AMOUNT.code);

            return electricityProcessResponse;
        }

        return payment(processRequestdto, electricityProcessRequest, accoutType);

    }

    @Override
    public ElectricityProcessResponse reQuery(ElectricityReQueryRequest electricityReQueryRequest) {

        log.info("================calling requery method");
        String url = baseUrl+requery;
        Gson gson = new Gson();

        Map<String, String> headers = new HashMap<>();
        headers.put("api-key", apiKey);
        headers.put("secret-key", secretKey);
        String payerId = electricityReQueryRequest.getPayerId();

        ElectricityProcessResponse electricityProcessResponse = new ElectricityProcessResponse();
        RequeryRequest requeryRequest = new RequeryRequest();
        requeryRequest.setRequest_id(electricityReQueryRequest.getUniqueTransId());

        String request = gson.toJson(requeryRequest);
        log.info("======================= the request sent for requery: "+request);

        String[] response = HTTPUtils.doPOSTRequest("POST", "application/json", url, request, headers, 5, false);

        if (response[0].equalsIgnoreCase("200")) {
            ProcessResponse processResponse = gson.fromJson(response[1], ProcessResponse.class);

            if (processResponse.getCode().equalsIgnoreCase("000")) {

                if(processResponse.getContent().getTransactions().getStatus().equalsIgnoreCase(Response.DELIVERED.toString()))
                {
                    log.info("================== the requery is delivered that is completed");
                    //uncomment this and comment the one below
                    String[] redisParam = lemonpayRedisService.getValue(payerId).split("~");
                    lemonpayRedisService.removeAccount(payerId);
                    electricityProcessResponse.setBusinessUnit(redisParam[2]);
                    electricityProcessResponse.setCustomerAddress(redisParam[1]);
                    electricityProcessResponse.setResponseDesc(Response.SUCCESS.toString());
                    electricityProcessResponse.setResponseCode(Response.SUCCESS.code);
                    //electricityProcessResponse.setBalance();
                    electricityProcessResponse.setCustomerArrears(processResponse.getArrearsBalance());
                    //electricityProcessResponse.setDeductions(processResponse);
                    electricityProcessResponse.setDisco("EEDC");
                    electricityProcessResponse.setCustomerName(redisParam[0]);
                    electricityProcessResponse.setErrorCode("00");
                    //electricityProcessResponse.setFeederBand();
                    electricityProcessResponse.setExternalReference(processResponse.getInvoiceNumber());
                    MainTokenData mainTokenData = new MainTokenData();

                    mainTokenData.setAmount(processResponse.getContent().getTransactions().getUnit_price());
                    if (electricityReQueryRequest.getType().equalsIgnoreCase("prepaid")) {
                        mainTokenData.setToken(processResponse.getToken());

                        mainTokenData.setUnit(String.valueOf(processResponse.getUnits()));
                        mainTokenData.setVat(String.valueOf(processResponse.getVat()));
                        mainTokenData.setKctTokens(processResponse.getKct1() + "~" + processResponse.getKct2());

                        electricityProcessResponse.setTariff(redisParam[3]);
                        electricityProcessResponse.setMainToken(mainTokenData);
                        electricityProcessResponse.setBalance(processResponse.getArrearsBalance());

                    }
                } else if (processResponse.getContent().getTransactions().getStatus().equalsIgnoreCase(Response.INITIATED.toString()) || processResponse.getContent().getTransactions().getStatus().equalsIgnoreCase(Response.PENDING.toString())) {

                    log.info("======================= the requery tranaction is pending");
                    electricityProcessResponse.setResponseCode(Response.PENDING.code);
                    electricityProcessResponse.setResponseDesc(Response.PENDING.toString());

                    return electricityProcessResponse;
                }else{
                    log.info("============================= this requery transaction failed");
                    electricityProcessResponse.setResponseCode(Response.FAILED.code);
                    electricityProcessResponse.setResponseDesc(processResponse.getResponse_description());

                    return electricityProcessResponse;
                }
            }else if (processResponse.getCode().equalsIgnoreCase("099")){
                log.info("======================= the requery tranaction is pending");
                electricityProcessResponse.setResponseCode(Response.PENDING.code);
                electricityProcessResponse.setResponseDesc(Response.PENDING.toString());

                return electricityProcessResponse;
            }else{
                log.info("============================= this requery transaction failed");
                electricityProcessResponse.setResponseCode(Response.FAILED.code);
                electricityProcessResponse.setResponseDesc(processResponse.getResponse_description());

                return electricityProcessResponse;
            }
        }else{

            electricityProcessResponse.setResponseCode(Response.FAILED.code);
            electricityProcessResponse.setResponseDesc(response[1]);

            return electricityProcessResponse;
        }
        return electricityProcessResponse;
     }


    @Override
    public PingResponse ping(ElectricityQueryRequest electricityQueryRequest) {
        return null;
    }

    public ElectricityQueryResponse verify(ElectricityQueryRequest electricityQueryRequest){

        Gson gson = new Gson();
        String type = null;

        ElectricityQueryResponse electricityQueryResponse = new ElectricityQueryResponse();

        if(electricityQueryRequest.getType().equalsIgnoreCase("1")){

            type = "prepaid";
        }else if(electricityQueryRequest.getType().equalsIgnoreCase("2")){

            type = "postpaid";
        }else{

            electricityQueryResponse = new ElectricityQueryResponse();
            electricityQueryResponse.setResponseDesc(Response.INVALID_ACCOUNT_METER_TYPE.toString());
            electricityQueryResponse.setResponseCode(Response.FAILED.code);

            return electricityQueryResponse;
        }

        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setBillersCode(Long.parseLong(electricityQueryRequest.getPayerId()));
        queryRequest.setServiceID(serviceId);
        queryRequest.setType(type);

        String request = gson.toJson(queryRequest);
        Map<String, String> headers = new HashMap<>();
        headers.put("api-key", apiKey);
        headers.put("secret-key",secretKey);

        String url = baseUrl +query;

        log.info("========================== the request to vtpas"+request);

        log.info("==========++================== start the call the eedc for query");
        String[] response = HTTPUtils.doPOSTRequest ( "POST", "application/json", url, request, headers,5, false);

        log.info("=============================== the response from calling enugu query is: "+response[0]+" "+response[1]);
        try {

            if (!response[0].equalsIgnoreCase("200")) {

                electricityQueryResponse = new ElectricityQueryResponse();
                electricityQueryResponse.setResponseDesc(response[1]);
                electricityQueryResponse.setResponseCode(response[0]);
            } else {

                EnuguElectricityQueryResponse enuguElectricityQueryResponse = gson.fromJson(response[1], EnuguElectricityQueryResponse.class);

                if (enuguElectricityQueryResponse.getCode().equalsIgnoreCase("000")) {

                    if(enuguElectricityQueryResponse.getContent().getError() != null){

                        electricityQueryResponse = new ElectricityQueryResponse();
                        electricityQueryResponse.setResponseDesc(enuguElectricityQueryResponse.getContent().getError());
                        electricityQueryResponse.setResponseCode(Response.FAILED.code);
                    }else {

                        String address = enuguElectricityQueryResponse.getContent().getAddress();
                        String minimumPurchase = enuguElectricityQueryResponse.getContent().getMin_Purchase_Amount();
                        String BusinessUnit = enuguElectricityQueryResponse.getContent().getDistrict();
                        String customerName = enuguElectricityQueryResponse.getContent().getCustomer_Name();

                        electricityQueryResponse.setResponseCode("00");
                        electricityQueryResponse.setResponseDesc(Response.SUCCESS.toString());
                        electricityQueryResponse.setAccountNumber(electricityQueryRequest.getPayerId());
                        electricityQueryResponse.setBusinessUnit(enuguElectricityQueryResponse.getContent().getDistrict());
                        electricityQueryResponse.setCustomerAddress(enuguElectricityQueryResponse.getContent().getAddress());
                        electricityQueryResponse.setCustomerName(customerName);
                        electricityQueryResponse.setMinimumPurchase(enuguElectricityQueryResponse.getContent().getMin_Purchase_Amount());
                        electricityQueryResponse.setTariff(enuguElectricityQueryResponse.getContent().getTariff());

                        String redisParam = address + "~" + minimumPurchase + "~" + BusinessUnit + "~" + enuguElectricityQueryResponse.getContent().getTariff() + "~" + customerName;
                        lemonpayRedisService.setValue(electricityQueryRequest.getPayerId(), redisParam, 3600L);
                    }
                } else {

                    electricityQueryResponse = new ElectricityQueryResponse();
                    electricityQueryResponse.setResponseDesc(enuguElectricityQueryResponse.getResponse_description());
                    electricityQueryResponse.setResponseCode(Response.FAILED.code);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            e.getMessage();

            electricityQueryResponse = new ElectricityQueryResponse();
            electricityQueryResponse.setResponseDesc(e.getMessage() != null?e.getMessage():Response.FAILED.toString());
            electricityQueryResponse.setResponseCode(Response.FAILED.code);

        }

        return electricityQueryResponse;
    }


    public ElectricityProcessResponse payment(ProcessRequestdto processRequestdto, ElectricityProcessRequest electricityProcessRequest, String actionType) {

        Map<String, String> headers = new HashMap<>();
        headers.put("api-key", apiKey);
        headers.put("secret-key", secretKey);
        String url = baseUrl + pay;

        Gson gson = new Gson();

        String request = gson.toJson(processRequestdto);

        log.info("================== the payload sent to enugu for payment is: "+gson.toJson(processRequestdto));

        String payerId = processRequestdto.getBillersCode();
        ElectricityProcessResponse electricityProcessResponse = new ElectricityProcessResponse();

        String redisValue = lemonpayRedisService.getValue(payerId);

        if(redisValue == null || redisValue.isEmpty()){

            electricityProcessResponse.setResponseCode(Response.FAILED.code);
            electricityProcessResponse.setResponseDesc(Response.PERFORM_QUERY_FIRST.toString());

            return electricityProcessResponse;
        }

        String[] redisParam = redisValue.split("~");

        String[] response = HTTPUtils.doPOSTRequest("POST", "application/json", url, request, headers, 5, false);

        if (response[0].equalsIgnoreCase("200")) {
            ProcessResponse processResponse = gson.fromJson(response[1], ProcessResponse.class);

            if (processResponse.getCode().equalsIgnoreCase("000")) {

                log.info("==============================transaction is successful");
                if(processResponse.getContent().getTransactions().getStatus().equalsIgnoreCase(Response.PENDING.toString())){

                    log.info("==================== transaction is pending call requery");
                    ElectricityReQueryRequest electricityReQueryRequest = new ElectricityReQueryRequest();
                    electricityReQueryRequest.setReference(processRequestdto.getRequest_id());
                    electricityReQueryRequest.setType(actionType);
                    //electricityReQueryRequest.setUniqueTransId("202508060042SqWceEdqaviL6");
                    electricityReQueryRequest.setPayerId(electricityProcessRequest.getPayerId());
                    electricityReQueryRequest.setAmount(electricityProcessRequest.getAmount());

                    electricityProcessResponse = reQuery(electricityReQueryRequest);

                    return electricityProcessResponse;
                }

                electricityProcessResponse.setAccountNumber(payerId);
                electricityProcessResponse.setAmount(String.valueOf(processRequestdto.getAmount()));
                electricityProcessResponse.setAccountType(actionType);
                //electricityProcessResponse.setAdminCharge();

                ////////////////remove the value from the redis
                lemonpayRedisService.removeAccount(payerId);
                electricityProcessResponse.setBusinessUnit(redisParam[2]);
                electricityProcessResponse.setCustomerAddress(redisParam[0]);
                electricityProcessResponse.setResponseDesc(Response.SUCCESS.toString());
                electricityProcessResponse.setResponseCode(Response.SUCCESS.code);
                //electricityProcessResponse.setBalance();
                electricityProcessResponse.setCustomerArrears(processResponse.getArrearsBalance());
                //electricityProcessResponse.setDeductions(processResponse);
                electricityProcessResponse.setDisco("EEDC");
                electricityProcessResponse.setMinmumPurchase(redisParam[1]);
                electricityProcessResponse.setCustomerName(redisParam[4]);
                electricityProcessResponse.setErrorCode("00");
                //electricityProcessResponse.setFeederBand();
                electricityProcessResponse.setReceiptNo(processResponse.getInvoiceNumber());
                electricityProcessResponse.setExternalReference(processRequestdto.getRequest_id());
                MainTokenData mainTokenData = new MainTokenData();

                mainTokenData.setAmount(processResponse.getContent().getTransactions().getUnit_price());
                if (actionType.equalsIgnoreCase("prepaid")) {
                    mainTokenData.setToken(processResponse.getToken());

                    mainTokenData.setUnit(String.valueOf(processResponse.getUnits()));
                    mainTokenData.setVat(String.valueOf(processResponse.getVat()));
                    mainTokenData.setKctTokens(processResponse.getKct1() + "~" + processResponse.getKct2());

                    electricityProcessResponse.setTariff(redisParam[3]);
                    electricityProcessResponse.setMainToken(mainTokenData);
                    electricityProcessResponse.setBalance(processResponse.getArrearsBalance());

                }
            } else if (processResponse.getCode().equalsIgnoreCase(Response.TRANSACTION_IS_REPROCESSING.code)) {
                /////////////
                log.info("==================== transaction is pending call requery");
                ElectricityReQueryRequest electricityReQueryRequest = new ElectricityReQueryRequest();
                electricityReQueryRequest.setReference(electricityProcessRequest.getReference());
                electricityReQueryRequest.setType(actionType);
                electricityReQueryRequest.setPayerId(electricityProcessRequest.getPayerId());
                electricityReQueryRequest.setAmount(electricityProcessRequest.getAmount());

                electricityProcessResponse = reQuery(electricityReQueryRequest);


            }else{
                log.info("====================== process failed");
                electricityProcessResponse.setResponseCode(Response.FAILED.code);
                electricityProcessResponse.setResponseDesc(processResponse.getResponse_description());
//                electricityProcessResponse.setErrors(processResponse.getContent().getErrors() !=null?processResponse.getContent().getErrors():null);
            }


        }else{
            electricityProcessResponse.setResponseCode(Response.FAILED.code);
            electricityProcessResponse.setResponseDesc(response[1]);
            log.info("======================= process failed");

        }
        return electricityProcessResponse;
    }
}
