package com.lemonpay.lemonpayvas.electricity.controller;

import com.lemonpay.lemonpayvas.electricity.dto.ElectricityGenericQueryRequest;
import com.lemonpay.lemonpayvas.electricity.dto.ElectricityGenericRequeryRequest;
import com.lemonpay.lemonpayvas.electricity.dto.ElectricityProcessorRequest;
import com.lemonpay.lemonpayvas.electricity.service.ElectricityProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/v2/vas/transaction")
public class ElectricityController {
    public final ElectricityProcessorService electricityProcessorService;

    public ElectricityController(ElectricityProcessorService electricityProcessorService) {
        this.electricityProcessorService = electricityProcessorService;
    }

    @PostMapping("/{alias}/query")
    public ResponseEntity<?> queryElectricity(@PathVariable String alias, @RequestBody ElectricityGenericQueryRequest request  ){
        return electricityProcessorService.query(request,alias);
    }

    @PostMapping("/{alias}/process")
    public ResponseEntity<?> ProcessElectricity(@PathVariable String alias,@Valid @RequestBody ElectricityProcessorRequest request  ){
        return electricityProcessorService.process(request,alias);  //////
    }

    @PostMapping("/{alias}/requery")
    public ResponseEntity<?> reProcessElectricity(@PathVariable String alias,@Valid @RequestBody ElectricityGenericRequeryRequest request  ){
        return electricityProcessorService.reProcess(request,alias);
    }

    @PostMapping("/{alias}/ping")
    public ResponseEntity<?> pingElectricity( @PathVariable String alias,@Valid @RequestBody ElectricityProcessorRequest request  ){
        return electricityProcessorService.ping(request,alias);
    }
//path changed
}
