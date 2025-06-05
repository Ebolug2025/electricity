package com.etranzact.vasgate.aedc.controller;

import com.etranzact.vasgate.aedc.service.AEDCProcessor;
import com.etranzact.vasgate.aedc.service.AEDCService;
import com.etranzact.vasgate.electricity.phcnnode.dto.ElectricityProcessRequest;
import com.etranzact.vasgate.electricity.phcnnode.dto.ElectricityProcessResponse;
import com.etranzact.vasgate.electricity.phcnnode.dto.ElectricityQueryRequest;
import com.etranzact.vasgate.electricity.phcnnode.dto.ElectricityQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/electricity")

public class AEDCController {
    @Autowired
    AEDCProcessor aedcProcessor;

    @PostMapping("/querymeter")
    public ElectricityQueryResponse query(@RequestBody ElectricityQueryRequest electricityQueryRequest){
        log.info("############## Query Meter #################");
        return aedcProcessor.query(electricityQueryRequest);
    }
    @PostMapping("/payment")
    public ElectricityProcessResponse process(@RequestBody ElectricityProcessRequest electricityProcessRequest){
        log.info("############## Process Meter #################");
        return aedcProcessor.process(electricityProcessRequest);
    }


}
