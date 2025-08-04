package com.lemonpay.lemonpayvas.electricity.ekedc.service;

import com.google.gson.Gson;
import com.lemonpay.lemonpayvas.electricity.ekedc.enums.Response;
import com.lemonpay.lemonpayvas.electricity.ekedc.requestdto.ProcessRequestdto;
import com.lemonpay.lemonpayvas.electricity.ekedc.requestdto.QueryRequest;
import com.lemonpay.lemonpayvas.electricity.ekedc.responsedto.EkoElectricityQueryResponse;
import com.lemonpay.lemonpayvas.electricity.ekedc.responsedto.ProcessResponse;
import com.lemonpay.lemonpayvas.electricity.ekedc.util.GenerateRequestId;
import com.lemonpay.lemonpayvas.electricity.ekedc.util.HTTPUtils;
import com.lemonpay.lemonpayvas.electricity.phcnnode.PHCNNode;
import com.lemonpay.lemonpayvas.electricity.phcnnode.dto.*;
import com.lemonpay.vasgate.electricity.redisutility.redisservice.LemonpayRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class EkoElectricityService extends PHCNNode {


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

        return payment(electricityProcessRequest, accoutType);

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
        String request = gson.toJson(electricityReQueryRequest);

        String[] response = HTTPUtils.doPOSTRequest("POST", "application/json", url, request, headers, 5, false);

        if (response[0].equalsIgnoreCase("200")) {
            ProcessResponse processResponse = gson.fromJson(response[1], ProcessResponse.class);

            if (processResponse.getCode().equalsIgnoreCase("000")) {

                String[] redisParam = lemonpayRedisService.getValue(payerId).split("~");
                lemonpayRedisService.removeAccount(payerId);
                electricityProcessResponse.setBusinessUnit(redisParam[2]);
                electricityProcessResponse.setCustomerAddress(redisParam[1]);
                electricityProcessResponse.setResponseDesc(Response.SUCCESS.toString());
                electricityProcessResponse.setResponseCode(Response.SUCCESS.code);
                //electricityProcessResponse.setBalance();
                electricityProcessResponse.setCustomerArrears(processResponse.getArrearsBalance());
                //electricityProcessResponse.setDeductions(processResponse);
                electricityProcessResponse.setDisco("EKEDC");
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
                    mainTokenData.setTokenTax(String.valueOf(processResponse.getMainTokenTax()));
                    mainTokenData.setKctTokens(processResponse.getKct1() + "~" + processResponse.getKct2());

                    BonusTokenData bonusTokenData = new BonusTokenData();
                    bonusTokenData.setAmount(processResponse.getBonusTokenAmount()!=null?String.valueOf(processResponse.getBonusTokenAmount()):null);
                    bonusTokenData.setToken(processResponse.getBonusToken());
                    bonusTokenData.setUnit(processResponse.getBonusTokenUnits());
                    bonusTokenData.setTokenTax(processResponse.getBonusToken()!=null?String.valueOf(processResponse.getBonusToken()):null);

                    electricityProcessResponse.setTariff(processResponse.getDebtTariff()!=null?String.valueOf(processResponse.getDebtTariff()):null);
                    electricityProcessResponse.setMainToken(mainTokenData);
                    electricityProcessResponse.setBonusToken(bonusTokenData);
                    electricityProcessResponse.setBalance(processResponse.getDebtAmount()!=null?String.valueOf(processResponse.getDebtAmount()):null);

                }
            }else if (processResponse.getCode().equalsIgnoreCase("099")){
                electricityProcessResponse.setResponseCode(Response.PENDING.code);
                electricityProcessResponse.setResponseDesc(Response.PENDING.toString());

                return electricityProcessResponse;
            }else{
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

        ElectricityQueryResponse electricityQueryResponse = new ElectricityQueryResponse();

        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setBillersCode(Long.parseLong(electricityQueryRequest.getPayerId()));
        queryRequest.setServiceID(serviceId);
        queryRequest.setType("prepaid");

        String request = gson.toJson(queryRequest);
        Map<String, String> headers = new HashMap<>();
        headers.put("api-key", secretKey);
        headers.put("secret-key",secretKey);

        String url = baseUrl +query;

        log.info("==========++================== start the call the ekedc for query");
        String[] response = HTTPUtils.doPOSTRequest ( "POST", "application/json", url, request, headers,5, false);

        log.info("=============================== the response from calling enugu query is: "+response[0]+" "+response[1]);
        if(!response[0].equalsIgnoreCase("200")){

            electricityQueryResponse = new ElectricityQueryResponse();
            electricityQueryResponse.setResponseDesc(response[1]);
            electricityQueryResponse.setResponseCode(response[0]);
        }else{

            EkoElectricityQueryResponse ekoElectricityQueryResponse = gson.fromJson(response[1], EkoElectricityQueryResponse.class);

            if(ekoElectricityQueryResponse.getCode().equalsIgnoreCase("000")) {
                String address = ekoElectricityQueryResponse.getContent().getAddress();
                String minimumPurchase = ekoElectricityQueryResponse.getContent().getMin_Purchase_Amount();
                String BusinessUnit = ekoElectricityQueryResponse.getContent().getBusiness_Unit();
                String customerName = ekoElectricityQueryResponse.getContent().getCustomer_Name();

                electricityQueryResponse.setResponseCode("00");
                electricityQueryResponse.setResponseDesc(Response.SUCCESS.toString());
                electricityQueryResponse.setAccountNumber(electricityQueryRequest.getPayerId());
                electricityQueryResponse.setBusinessUnit(ekoElectricityQueryResponse.getContent().getBusiness_Unit());
                electricityQueryResponse.setCustomerAddress(ekoElectricityQueryResponse.getContent().getAddress());
                electricityQueryResponse.setCustomerName(customerName);
                electricityQueryResponse.setMinimumPurchase(ekoElectricityQueryResponse.getContent().getMin_Purchase_Amount());
                electricityQueryResponse.setTariff(ekoElectricityQueryResponse.getContent().getTariff());

                String redisParam = address + "~" + minimumPurchase + "~" + BusinessUnit + "~" + ekoElectricityQueryResponse.getContent().getTariff();
                lemonpayRedisService.setValue(electricityQueryRequest.getPayerId(), redisParam);
            }else{
                electricityQueryResponse = new ElectricityQueryResponse();
                electricityQueryResponse.setResponseDesc(Response.FAILED.code);
                electricityQueryResponse.setResponseCode(ekoElectricityQueryResponse.getResponse_description());
            }

        }

        return electricityQueryResponse;
    }


    public ElectricityProcessResponse payment(ElectricityProcessRequest electricityProcessRequest, String actionType) {

        Map<String, String> headers = new HashMap<>();
        headers.put("api-key", apiKey);
        headers.put("secret-key", secretKey);
        String url = baseUrl + pay;

        Gson gson = new Gson();

        String request = gson.toJson(electricityProcessRequest);

        log.info("================== the payload sent to eko for payment is: "+gson.toJson(electricityProcessRequest));

        String payerId = electricityProcessRequest.getPayerId();
        ElectricityProcessResponse electricityProcessResponse = new ElectricityProcessResponse();

        String[] response = HTTPUtils.doPOSTRequest("POST", "application/json", url, request, headers, 5, false);

        if (response[0].equalsIgnoreCase("200")) {
            ProcessResponse processResponse = gson.fromJson(response[1], ProcessResponse.class);

            if (processResponse.getCode().equalsIgnoreCase("000")) {


                electricityProcessResponse.setAccountNumber(electricityProcessRequest.getPayerId());
                electricityProcessResponse.setAmount(String.valueOf(electricityProcessRequest.getAmount()));
                electricityProcessResponse.setAccountType(actionType);
                //electricityProcessResponse.setAdminCharge();
                String[] redisParam = lemonpayRedisService.getValue(payerId).split("~");

                ////////////////remove the value from the redis
                lemonpayRedisService.removeAccount(payerId);
                electricityProcessResponse.setBusinessUnit(redisParam[2]);
                electricityProcessResponse.setCustomerAddress(redisParam[1]);
                electricityProcessResponse.setResponseDesc(Response.SUCCESS.toString());
                electricityProcessResponse.setResponseCode(Response.SUCCESS.code);
                //electricityProcessResponse.setBalance();
                electricityProcessResponse.setCustomerArrears(processResponse.getArrearsBalance());
                //electricityProcessResponse.setDeductions(processResponse);
                electricityProcessResponse.setDisco("EKEDC");
                electricityProcessResponse.setCustomerName(redisParam[0]);
                electricityProcessResponse.setErrorCode("00");
                //electricityProcessResponse.setFeederBand();
                electricityProcessResponse.setExternalReference(processResponse.getExchangeReference());
                MainTokenData mainTokenData = new MainTokenData();

                mainTokenData.setAmount(processResponse.getContent().getTransactions().getUnit_price());
                if (actionType.equalsIgnoreCase("prepaid")) {
                    mainTokenData.setToken(processResponse.getToken());

                    mainTokenData.setUnit(String.valueOf(processResponse.getUnits()));
                    mainTokenData.setVat(String.valueOf(processResponse.getVat()));
                    mainTokenData.setTokenTax(String.valueOf(processResponse.getMainTokenTax()));
                    mainTokenData.setKctTokens(processResponse.getKct1() + "~" + processResponse.getKct2());

                    BonusTokenData bonusTokenData = new BonusTokenData();
                    bonusTokenData.setAmount(processResponse.getBonusTokenAmount()!=null?String.valueOf(processResponse.getBonusTokenAmount()):null);
                    bonusTokenData.setToken(processResponse.getBonusToken());
                    bonusTokenData.setUnit(processResponse.getBonusTokenUnits());
                    bonusTokenData.setTokenTax(processResponse.getBonusToken()!=null?String.valueOf(processResponse.getBonusToken()):null);

                    electricityProcessResponse.setTariff(processResponse.getDebtTariff()!=null?String.valueOf(processResponse.getDebtTariff()):null);
                    electricityProcessResponse.setMainToken(mainTokenData);
                    electricityProcessResponse.setBonusToken(bonusTokenData);
                    electricityProcessResponse.setBalance(processResponse.getDebtAmount()!=null?String.valueOf(processResponse.getDebtAmount()):null);

                }
            } else if (processResponse.getCode().equalsIgnoreCase(Response.TRANSACTION_IS_REPROCESSING.code)) {
                /////////////
                log.info("==================== transaction is pending call requery");
                ElectricityReQueryRequest electricityReQueryRequest = new ElectricityReQueryRequest();
                electricityReQueryRequest.setReference(electricityProcessRequest.getReference());
                electricityReQueryRequest.setType(actionType);
                electricityReQueryRequest.setPayerId(electricityProcessRequest.getPayerId());
                electricityReQueryRequest.setAmount(electricityProcessRequest.getAmount());
                /////////////////////////////////////////////////call requery
                electricityProcessResponse = reQuery(electricityReQueryRequest);


            }else{
                electricityProcessResponse.setResponseCode(Response.FAILED.code);
                electricityProcessResponse.setResponseDesc(processResponse.getResponse_description());

            }


        }else{
            electricityProcessResponse.setResponseCode(Response.FAILED.code);
            electricityProcessResponse.setResponseDesc(response[1]);

        }
        return electricityProcessResponse;
    }
}
