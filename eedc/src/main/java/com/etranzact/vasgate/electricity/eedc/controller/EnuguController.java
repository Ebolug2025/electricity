package com.etranzact.vasgate.electricity.eedc.controller;

import com.etranzact.vasgate.electricity.eedc.service.EedcProcessor;
import com.etranzact.vasgate.electricity.phcnnode.dto.ElectricityProcessRequest;
import com.etranzact.vasgate.electricity.phcnnode.dto.ElectricityProcessResponse;
import com.etranzact.vasgate.electricity.phcnnode.dto.ElectricityQueryRequest;
import com.etranzact.vasgate.electricity.phcnnode.dto.ElectricityQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class EnuguController {
    @Autowired
    private EedcProcessor eedcProcessor;

    public EnuguController(EedcProcessor eedcProcessor) {
        this.eedcProcessor = eedcProcessor;
    }

    @PostMapping("/querymeter")
    public ElectricityQueryResponse queryMeter(@RequestBody ElectricityQueryRequest electricityQueryRequest){

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> request sent to query meter is received");

        return eedcProcessor.query(electricityQueryRequest);
    }

    @PostMapping("/payment")
    public ElectricityProcessResponse electricityPayment(@RequestBody ElectricityProcessRequest electricityProcessRequest){

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> request sent to make payment is received" );

        return eedcProcessor.process(electricityProcessRequest);
    }

}
