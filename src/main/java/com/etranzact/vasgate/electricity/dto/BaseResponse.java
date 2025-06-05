package com.etranzact.vasgate.electricity.dto;

import lombok.Data;

@Data
public class BaseResponse {
    private String status;
    private String message;
    private Object data;
}
