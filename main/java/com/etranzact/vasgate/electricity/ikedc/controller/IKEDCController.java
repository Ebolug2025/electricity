package com.etranzact.vasgate.electricity.ikedc.controller;

import com.etranzact.vasgate.electricity.phcnnode.dto.ElectricityProcessRequest;
import com.etranzact.vasgate.electricity.phcnnode.dto.ElectricityProcessResponse;
import com.etranzact.vasgate.electricity.phcnnode.dto.ElectricityQueryRequest;
import com.etranzact.vasgate.electricity.phcnnode.dto.ElectricityQueryResponse;
import com.etranzact.vasgate.electricity.ikedc.core.IKEDCProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/electricity")
@Slf4j
public class IKEDCController {

    @Autowired
    private IKEDCProcessor ikedcProcessor;

    @PostMapping("/query")
    ElectricityQueryResponse query(ElectricityQueryRequest electricityQueryRequest){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> request received at the controller for");
        return ikedcProcessor.query(electricityQueryRequest);
    }

    @PostMapping("/process")
    ElectricityProcessResponse query(ElectricityProcessRequest electricityProcessRequest){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> request received at the controller for");
        return ikedcProcessor.process(electricityProcessRequest);
    }

//    @GetMapping("/requery")
//    ElectricityQueryResponse requery(String accountNumber){
//        return ikedcProcessor.requery(accountNumber);
//    }
}
