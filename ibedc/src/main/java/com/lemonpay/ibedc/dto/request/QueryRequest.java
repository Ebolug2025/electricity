package com.lemonpay.ibedc.dto.request;

import com.lemonpay.ibedc.dto.response.QueryContent;
import com.lemonpay.ibedc.dto.response.ResponseContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryRequest {
//    private Long billersCode;
//    private String serviceID;
//    private String type;

    private String code;
    private QueryContent content;
}
