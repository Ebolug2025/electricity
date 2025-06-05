package com.etranzact.vasgate.electricity.ekoedc.Controller;


import com.etranzact.vasgate.electricity.ekoedc.Domain.Request.ChangePasswordReq;
import com.etranzact.vasgate.electricity.ekoedc.Domain.Request.CustEnqRequest;
//import com.etranzact.vasgate.electricity.ekoedc.Domain.Request.ElectricityReQueryRequest;
import com.etranzact.vasgate.electricity.ekoedc.Domain.Request.ShiftEnquiryRequest;
import com.etranzact.vasgate.electricity.ekoedc.Domain.Response.BaseResponse;
import com.etranzact.vasgate.electricity.ekoedc.EKOEDCService;
import com.etranzact.vasgate.electricity.phcnnode.dto.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/electricity")
@AllArgsConstructor
public class EKOEDCController {

    @Autowired
    EKOEDCService ekoedcService;

    @PostMapping("/querymeter")
    public ElectricityQueryResponse query(@RequestBody ElectricityQueryRequest electricityQueryRequest) {
        log.info("*******INSIDE query controller ************** ");
        return ekoedcService.query(electricityQueryRequest);
    }

    @PostMapping("/payment")
    public ElectricityProcessResponse process(@RequestBody ElectricityProcessRequest electricityProcessRequest) {
        log.info("*******INSIDE process controller ************** ");
        return ekoedcService.process(electricityProcessRequest);
    }


    @PostMapping("/requery")
    public ElectricityProcessResponse reQuery(@RequestBody ElectricityReQueryRequest electricityReProcessRequest) {
        log.info("*******INSIDE reQuery controller ************** ");
        return ekoedcService.reQuery(electricityReProcessRequest);
    }

}
