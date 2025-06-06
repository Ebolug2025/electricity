package com.lemonpay.lemonpayvas.electricity.ikedc.service;

import com.google.gson.Gson;
import com.lemonpay.lemonpayvas.electricity.ikedc.requestdto.QueryRequest;
import com.lemonpay.lemonpayvas.electricity.phcnnode.PHCNNode;
import com.lemonpay.lemonpayvas.electricity.phcnnode.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class IkejaElectricityService extends PHCNNode {

    @Value("${serviceId}")
    private String serviceId;
    @Value("${api-key}")
    private String apiKey;
    @Value("${secret-key}")
    private String secretKey;

    @Override
    public ElectricityQueryResponse query(ElectricityQueryRequest electricityQueryRequest) {

        Gson gson = new Gson();
        log.info("============================= The electricity query request is: "+gson.toJson(electricityQueryRequest));
      QueryRequest queryRequest = new QueryRequest();
      queryRequest.setServiceID(serviceId);

        return null;
    }

    @Override
    public ElectricityProcessResponse process(ElectricityProcessRequest electricityProcessRequest) {
        return null;
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
