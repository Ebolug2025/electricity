package com.etranzact.vasgate.electricity.ekoedc.Domain.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T>{
    private String status;
    private String message;
    private T data;
    public BaseResponse(String status) {
        this.status = status;
    }

    public BaseResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public BaseResponse(String status, String message, T result) {
        this.status = status;
        this.message = message;
        this.data = result;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }


}
