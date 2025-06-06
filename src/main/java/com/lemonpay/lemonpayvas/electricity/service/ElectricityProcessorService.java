package com.lemonpay.lemonpayvas.electricity.service;

import com.lemonpay.lemonpayvas.electricity.dto.BaseResponse;
import com.lemonpay.lemonpayvas.electricity.dto.ElectricityProcessorRequest;
import org.springframework.http.ResponseEntity;

public interface ElectricityProcessorService {
    ResponseEntity<BaseResponse> query(ElectricityProcessorRequest request, String alias);

    ResponseEntity<BaseResponse> process(ElectricityProcessorRequest request, String alias);

    ResponseEntity<BaseResponse> reProcess(ElectricityProcessorRequest request, String alias);

    ResponseEntity<BaseResponse> ping(ElectricityProcessorRequest request, String alias);
}
