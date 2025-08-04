package com.lemonpay.lemonpayvas.electricity.service;

import com.lemonpay.lemonpayvas.electricity.dto.BaseResponse;
import com.lemonpay.lemonpayvas.electricity.dto.ElectricityGenericQueryRequest;
import com.lemonpay.lemonpayvas.electricity.dto.ElectricityGenericRequeryRequest;
import com.lemonpay.lemonpayvas.electricity.dto.ElectricityProcessorRequest;
import org.springframework.http.ResponseEntity;

public interface ElectricityProcessorService {
    ResponseEntity<BaseResponse> query(ElectricityGenericQueryRequest request, String alias);

    ResponseEntity<BaseResponse> process(ElectricityProcessorRequest request, String alias);

    ResponseEntity<BaseResponse> reProcess(ElectricityGenericRequeryRequest request, String alias);

    ResponseEntity<BaseResponse> ping(ElectricityProcessorRequest request, String alias);
}
