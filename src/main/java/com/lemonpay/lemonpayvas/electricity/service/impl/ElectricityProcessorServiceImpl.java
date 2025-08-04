package com.lemonpay.lemonpayvas.electricity.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemonpay.lemonpayvas.electricity.dto.*;
import com.lemonpay.lemonpayvas.electricity.entity.VasTrans;
import com.lemonpay.lemonpayvas.electricity.exception.ResourceNotFoundException;
import com.lemonpay.lemonpayvas.electricity.phcnnode.PHCNNode;
import com.lemonpay.lemonpayvas.electricity.phcnnode.dto.*;
import com.lemonpay.lemonpayvas.electricity.repository.VasTransRepo;
import com.lemonpay.lemonpayvas.electricity.service.ElectricityProcessorService;
import com.lemonpay.lemonpayvas.electricity.telco.repo.TPhcnDistrictPostpaidRepo;
import com.lemonpay.lemonpayvas.electricity.telco.repo.TPhcnDistrictPrepaidRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
//@Profile("live")
public class ElectricityProcessorServiceImpl implements ElectricityProcessorService {


    private final ApplicationContext context;


    private final TPhcnDistrictPostpaidRepo postpaidRepo;


    private final TPhcnDistrictPrepaidRepo prepaidRepo;


    private final VasTransRepo vasTransRepo;

    public ElectricityProcessorServiceImpl(ApplicationContext context, TPhcnDistrictPostpaidRepo postpaidRepo, TPhcnDistrictPrepaidRepo prepaidRepo, VasTransRepo vasTransRepo) {
        this.context = context;
        this.postpaidRepo = postpaidRepo;
        this.prepaidRepo = prepaidRepo;
        this.vasTransRepo = vasTransRepo;
    }

    @Override
    public ResponseEntity<BaseResponse> query(ElectricityGenericQueryRequest request, String alias) {
        System.out.println("::::::::::::::::::::::::Alias::::::::::::::::"+alias);
        // Determine which module to use based on transaction details
        PHCNNode phcnNode;
        try{
         phcnNode = context.getBean(alias,PHCNNode.class);
        }catch (NoSuchBeanDefinitionException ex) {
            throw new ResourceNotFoundException(alias + " Node not available :: ");
        }

        if( request.getAlias()==null){
            request.setAlias(alias);
        }
        BaseResponse response = new BaseResponse();

        //check required data is sent
        if(request.getType().isEmpty()){
            response.setMessage("Type is required");
            response.setStatus(ResponseEnum.BAD_REQUEST.getResponseCode());
            return ResponseEntity.ok(response);
        }else if(request.getReference().isEmpty()){
            response.setMessage("Reference is required");
            response.setStatus(ResponseEnum.BAD_REQUEST.getResponseCode());
            return ResponseEntity.ok(response);
        }else if(request.getPayerId().isEmpty()){
            response.setMessage("PayerId is required");
            response.setStatus(ResponseEnum.BAD_REQUEST.getResponseCode());
            return ResponseEntity.ok(response);
        }else if(request.getChannel().isEmpty()){
            response.setMessage("Channel is required");
            response.setStatus(ResponseEnum.BAD_REQUEST.getResponseCode());
            return ResponseEntity.ok(response);
        }

        //process electricity
        ElectricityQueryRequest electricityRequest = new ElectricityQueryRequest();
        electricityRequest.setPayerId(request.getPayerId());
        electricityRequest.setAmount(request.getAmount() == null? 0: request.getAmount());
        electricityRequest.setReference(request.getReference());
        electricityRequest.setType(request.getType());
        electricityRequest.setChannel(request.getChannel());
        ////electricityRequest.setChannel(request.getAction());
        electricityRequest.setMobile(request.getMobile());

        //call disco depending on alias
        ElectricityQueryResponse nodeResponse;
        try{
         nodeResponse = phcnNode.query(electricityRequest);
        }catch (NoSuchBeanDefinitionException ex) {
            throw new ResourceNotFoundException(alias + " Node Exception ");
        }


        log.info("*********** RESPONSE FROM NODE::::::::::: "+nodeResponse);

        if(nodeResponse.getResponseCode() == "00") {
            //getMerchantCode
//            String merchantCode = null;
//            String disco;
//            if (alias.length() >= 3) {
//                disco = alias.substring(alias.length() - 3).toUpperCase();
//                System.out.println("Last three characters: " + disco);
//                log.info("Last three characters: " + disco);
//            } else {
//                disco = alias;
//                System.out.println("String is too short to get the last three characters");
//                log.info("String is too short to get the last three characters");
//            }

//            String channelName = "";
//            if (!request.getChannel().equals("01") && !request.getChannel().equals("09")) {
//                if (request.getChannel().equals("02")) {
//                    if (request.getReference().contains("02USD")) {
//                        channelName = "USSD";
//                    } else if (request.getReference().startsWith("02POS")) {
//                        channelName = "POS";
//                    } else {
//                        channelName = "MOBILE";
//                    }
//                } else if (request.getChannel().equals("03")) {
//                    channelName = "POS";
//                } else if (request.getChannel().equals("11")) {
//                    channelName = "USSD";
//                } else if (request.getChannel().equals("05")) {
//                    channelName = "PAYOUTLET";
//                }
//            } else {
//                channelName = "WEB";
//            }
//
//            if (request.getType().equals("1")) {
//                String busUnit = null;
//
//                if (nodeResponse.getBusinessUnit().toLowerCase().contains("region")) {
//                    busUnit = nodeResponse.getBusinessUnit().substring(0, nodeResponse.getBusinessUnit().toLowerCase().indexOf("region")).trim().replaceAll(" ", "-");
//                } else {
//                    busUnit = nodeResponse.getBusinessUnit().substring(0, nodeResponse.getBusinessUnit().indexOf(" "));
//                }
//
//                log.info("==========================getting merchant code with the parameters: "+disco+" "+busUnit);
                //Map<String, Object> prepaid = prepaidRepo.getMerchantCode(disco, busUnit.trim()+"%");
//                if (channelName.equals("WEB")) {
//                    merchantCode = (String) prepaid.get("WEB_MERCHANT_CODE");
//                } else if (channelName.equals("POS")) {
//                    merchantCode = (String) prepaid.get("POS_MERCHANT_CODE");
//                } else if (channelName.equals("PAYOUTLET")) {
//                    merchantCode = (String) prepaid.get("PAYOUTLET_MERCHANT_CODE");
//                } else if (channelName.equals("USSD")) {
//                    merchantCode = (String) prepaid.get("USSD_MERCHANT_CODE");
//                } else if (channelName.equals("MOBILE")) {
//                    merchantCode = (String) prepaid.get("MOBILE_MERCHANT_CODE");
//                }

//                log.info("=========================== the returned merchantcode is: "+merchantCode);
//            } else if (request.getType().equals("2")) {
//                String busUnit = null;
//
//                if (nodeResponse.getBusinessUnit().toLowerCase().contains("region")) {
//                    busUnit = nodeResponse.getBusinessUnit().substring(0, nodeResponse.getBusinessUnit().toLowerCase().indexOf("region")).trim().replaceAll(" ", "-");
//                } else {
//                    busUnit = nodeResponse.getBusinessUnit().substring(0, nodeResponse.getBusinessUnit().indexOf(" "));
//                }
//
//                log.info("==========================getting merchant code with the parameters: "+disco+" "+busUnit+" channel "+channelName);
//                Map<String, Object> postpaid = postpaidRepo.getMerchantCode(disco, busUnit.trim()+"%");
//               // log.info("========================== fetched merchant codes is: "+postpaid);
//                if (channelName.equals("WEB")) {
//                    merchantCode = (String) postpaid.get("WEB_MERCHANT_CODE");
//                } else if (channelName.equals("POS")) {
//                    merchantCode = (String) postpaid.get("POS_MERCHANT_CODE");
//                } else if (channelName.equals("PAYOUTLET")) {
//                    merchantCode = (String) postpaid.get("PAYOUTLET_MERCHANT_CODE");
//                } else if (channelName.equals("USSD")) {
//                    merchantCode = (String) postpaid.get("USSD_MERCHANT_CODE");
//                } else if (channelName.equals("MOBILE")) {
//                    merchantCode = (String) postpaid.get("MOBILE_MERCHANT_CODE");
//                }
//                log.info("=========================== the returned merchantcode is: "+merchantCode);
//            }
//            nodeResponse.setMerchantCode(merchantCode);

            //    BaseResponse response = new BaseResponse();
            response.setMessage(ResponseEnum.SUCCESSFUL.getResponseMessage());
            response.setStatus(ResponseEnum.SUCCESSFUL.getResponseCode());
            response.setData(nodeResponse);
        }else{
            response.setMessage(nodeResponse.getResponseDesc());
            response.setStatus(ResponseEnum.BAD_REQUEST.getResponseCode());
            response.setData(nodeResponse);
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<BaseResponse> process(ElectricityProcessorRequest request, String alias) {
        System.out.println("Alias::::::::::::::::"+alias);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info("::::::::::::::::::::::::::::::::::::Electricity request"+objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {

            throw new RuntimeException(e);
        }
        // Determine which module to use based on transaction details
        PHCNNode phcnNode;
        try{
            phcnNode = context.getBean(alias,PHCNNode.class);
        }catch (NoSuchBeanDefinitionException ex) {
            throw new ResourceNotFoundException(alias + " Node not available ");
        }

        if( request.getAlias()==null){
            request.setAlias(alias);
        }
        BaseResponse response = new BaseResponse();

        //check required data is sent
        if(request.getType().isEmpty()){
            response.setMessage("Type is required");
            response.setStatus(ResponseEnum.BAD_REQUEST.getResponseCode());
            return ResponseEntity.ok(response);
        }else if(request.getReference().isEmpty()){
            response.setMessage("Reference is required");
            response.setStatus(ResponseEnum.BAD_REQUEST.getResponseCode());
            return ResponseEntity.ok(response);
        }else if(request.getPayerId().isEmpty()){
            response.setMessage("PayerId is required");
            response.setStatus(ResponseEnum.BAD_REQUEST.getResponseCode());
            return ResponseEntity.ok(response);
        }else if(request.getChannel().isEmpty()){
            response.setMessage("Channel is required");
            response.setStatus(ResponseEnum.BAD_REQUEST.getResponseCode());
            return ResponseEntity.ok(response);
        }else if(request.getMobile().isEmpty()){
            response.setMessage("Mobile is required");
            response.setStatus(ResponseEnum.BAD_REQUEST.getResponseCode());
            return ResponseEntity.ok(response);
        } else if(request.getAmount() == null){
            response.setMessage("Amount is required");
            response.setStatus(ResponseEnum.BAD_REQUEST.getResponseCode());
            return ResponseEntity.ok(response);
        }

        //process electricity
        ElectricityProcessRequest electricityRequest = new ElectricityProcessRequest();
        electricityRequest.setPaymentChannel(request.getAlias());
        electricityRequest.setPayerId(request.getPayerId());
        electricityRequest.setAmount(request.getAmount());
        electricityRequest.setReference(request.getReference());
        electricityRequest.setType(request.getType());
        electricityRequest.setChannel(request.getChannel());
        electricityRequest.setMobile(request.getMobile());
        electricityRequest.setName(request.getName());
      //  electricityRequest.setAction(request.getAction());

        //check if referencealready exists
       Optional<VasTrans> trans = vasTransRepo.findByUniqueTransId(request.getReference());
       if(trans.isPresent()){
           response.setMessage("Duplicate Reference");
           response.setStatus("400");
           //response
           /////////////duplicate reponse
           return ResponseEntity.ok(response);
       }

        //save transaction
        VasTrans vasTrans = new VasTrans();
        //vasTrans.setMerchantId(alias);
//        vasTrans.setMerchantCode(request.getMerchant());
        vasTrans.setTransDate(new Date());
        vasTrans.setTransType("00");
        vasTrans.setTransChannel(request.getChannel());
        vasTrans.setTransAmount(request.getAmount());
        vasTrans.setMobileNo(request.getMobile());
        vasTrans.setPaymentType(request.getType());
        vasTrans.setTAddress(request.getCustomerAddress());

        ElectricityProcessResponse nodeResponse = phcnNode.process(electricityRequest);

        try {
            log.info(":::::::::::::::::::::::Electricity process response "+objectMapper.writeValueAsString(nodeResponse));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String processStatus ;
        if(nodeResponse.getResponseCode().equals("00")){
            processStatus = "0";
        }else if(nodeResponse.getResponseCode().equals("01")){
            processStatus = "4";
        }else if(nodeResponse.getResponseCode().equals("07")){
            processStatus = "7";
        }else if(nodeResponse.getResponseCode().equals("06")){
            processStatus = "6";
        }else{
            processStatus = "1";
        }

        vasTrans.setUniqueTransid(request.getReference());
        vasTrans.setTransNo(nodeResponse.getExternalReference());
        vasTrans.setStatusDescription(nodeResponse.getResponseDesc());
        vasTrans.setTransStatus(processStatus);
        vasTrans.setResponseDate(new Date());

        vasTransRepo.save(vasTrans);

        //BaseResponse response = new BaseResponse();
        response.setMessage(ResponseEnum.SUCCESSFUL.getResponseMessage());
        response.setStatus(ResponseEnum.SUCCESSFUL.getResponseCode());
        response.setData(nodeResponse);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<BaseResponse> reProcess(ElectricityGenericRequeryRequest request, String alias) {
        System.out.println("Alias::::::::::::::::" + alias);
        BaseResponse response = new BaseResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info(":::::::::::::::::::::::Electricity requery request"+objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

      //  objectMapper

        //check required data is sent
        if(request.getReference().isEmpty()){
            response.setMessage(ResponseEnum.BAD_REQUEST.getResponseMessage());
            response.setStatus("UniqueTransId is required");
            return ResponseEntity.ok(response);
        }

        Optional<VasTrans> optionalTPaytrans = vasTransRepo.findByUniqueTransId(request.getReference());

        // Determine which module to use based on transaction details
        PHCNNode phcnNode;
        try{
            phcnNode = context.getBean(alias,PHCNNode.class);
        }catch (NoSuchBeanDefinitionException ex) {
            throw new ResourceNotFoundException(alias + " Node not available ");
        }

        if (request.getAlias() == null) {
            request.setAlias(alias);
        }

        if (!optionalTPaytrans.isPresent()) {

            response.setMessage(ResponseEnum.NOT_FOUND.getResponseMessage());
            response.setStatus(ResponseEnum.NOT_FOUND.getResponseCode());

        }else{
            //if present check the transaction status
            VasTrans vasTrans = optionalTPaytrans.get();

            log.info("::::::::::::::::::::::::::::");
            if(!(vasTrans.getTransStatus().equals("0"))) {

                //call disco depending on alias
                ElectricityReQueryRequest electricityRequest = new ElectricityReQueryRequest();

                electricityRequest.setPaymentChannel(request.getAlias());
                electricityRequest.setPayerId(request.getPayerId());
                electricityRequest.setAmount(request.getAmount()== null?0: request.getAmount()); ////////////
                electricityRequest.setReference(request.getReference());
                electricityRequest.setType(request.getType());
                electricityRequest.setChannel(request.getChannel());
                electricityRequest.setMobile(request.getMobile());
               // electricityRequest.setAction(request.getAction());
                electricityRequest.setUniqueTransId(vasTrans.getTransNo());

                ElectricityProcessResponse nodeResponse;
                try{
                 nodeResponse = phcnNode.reQuery(electricityRequest);
                }catch (NoSuchBeanDefinitionException ex) {
                    throw new ResourceNotFoundException(alias + " Node Exception ");
                }

                //update transaction
                String processStatus ;
                if(nodeResponse.getResponseCode().equals("00")){
                    processStatus = "0";
                }else if(nodeResponse.getResponseCode().equals("01")){
                    processStatus = "4";
                }else if(nodeResponse.getResponseCode().equals("07")){
                    processStatus = "7";
                }else if(nodeResponse.getResponseCode().equals("06")){
                    processStatus = "6";
                }else{
                    processStatus = "1";
                }
                vasTrans.setUniqueTransid(nodeResponse.getUniqueTransId());
                vasTrans.setTransStatus(processStatus);
                vasTrans.setStatusDescription(nodeResponse.getResponseDesc());
                vasTrans.setResponseDate(new Date());
                //vasTrans.setChequeBank(nodeResponse.getExternalReference()); //external reference
                vasTransRepo.save(vasTrans);
                response.setMessage(ResponseEnum.SUCCESSFUL.getResponseMessage());
                response.setStatus(ResponseEnum.SUCCESSFUL.getResponseCode());
                response.setData(nodeResponse);

            }else{

                ElectricityProcessResponse nodeResponse = new ElectricityProcessResponse();
                log.info("***********RESPONSE FROM NODE::::::::::: "+nodeResponse);
                nodeResponse.setAmount(String.valueOf(vasTrans.getTransAmount()));
                nodeResponse.setDisco(vasTrans.getAlias());
                nodeResponse.setAccountNumber(vasTrans.getAccount());
                nodeResponse.setUniqueTransId(vasTrans.getUniqueTransid());
                nodeResponse.setResponseCode(vasTrans.getTransStatus());
                nodeResponse.setExternalReference(vasTrans.getUniqueTransid());
                nodeResponse.setResponseDesc(vasTrans.getStatusDescription());

                String processStatus ;
                if(vasTrans.getTransStatus().equals("0")){
                    processStatus = "00";
                }else if(nodeResponse.getResponseCode().equals("4")){
                    processStatus = "01";
                }else if(nodeResponse.getResponseCode().equals("7")){
                    processStatus = "07";
                }else if(nodeResponse.getResponseCode().equals("6")){
                    processStatus = "06";
                }else{
                    processStatus = "1";
                }
                nodeResponse.setResponseCode(processStatus);

                response.setMessage(ResponseEnum.SUCCESSFUL.getResponseMessage());
                response.setStatus(ResponseEnum.SUCCESSFUL.getResponseCode());
                response.setData(nodeResponse);
            }

        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<BaseResponse> ping(ElectricityProcessorRequest request, String alias) {

        PHCNNode phcnNode;
        try{
            phcnNode = context.getBean(alias,PHCNNode.class);
        }catch (NoSuchBeanDefinitionException ex) {
            throw new ResourceNotFoundException(alias + " Node not available ");
        }

        if( request.getAlias()==null){
            request.setAlias(alias);
        }

        //process electricity
        ElectricityQueryRequest electricityRequest = new ElectricityQueryRequest();
        electricityRequest.setPayerId(request.getPayerId());
       // electricityRequest.setAmount(request.getAmount());
        electricityRequest.setReference(request.getReference());
        electricityRequest.setType(request.getType());
        electricityRequest.setChannel(request.getChannel());
       // electricityRequest.setAction(request.getAction());
        electricityRequest.setMobile(request.getMobile());

        //call disco depending on alias
        PingResponse nodeResponse;
        try {
            nodeResponse = phcnNode.ping(electricityRequest);
        }catch (NoSuchBeanDefinitionException ex) {
        throw new ResourceNotFoundException(alias + " Node Exception ");
        }

        log.info("*********** RESPONSE FROM NODE::::::::::: "+nodeResponse);

        BaseResponse response = new BaseResponse();
        response.setMessage(nodeResponse.getMessage());
        response.setStatus(nodeResponse.getCode());
        return ResponseEntity.ok(response);
    }

//cloned
}
