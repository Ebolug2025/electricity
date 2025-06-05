package com.etranzact.vasgate.electricity.service;

import com.etranzact.vasgate.electricity.dto.BaseResponse;
import com.etranzact.vasgate.electricity.dto.ElectricityProcessorRequest;
import org.springframework.http.ResponseEntity;

public interface ElectricityProcessorService {
    ResponseEntity<BaseResponse> query(ElectricityProcessorRequest request, String alias);

    ResponseEntity<BaseResponse> process(ElectricityProcessorRequest request, String alias);

    ResponseEntity<BaseResponse> reProcess(ElectricityProcessorRequest request, String alias);

    ResponseEntity<BaseResponse> ping(ElectricityProcessorRequest request, String alias);
}
