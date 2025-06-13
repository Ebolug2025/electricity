package com.lemonpay.ibedc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryResponse {
    private String code;
    private ResponseContent content;
}
