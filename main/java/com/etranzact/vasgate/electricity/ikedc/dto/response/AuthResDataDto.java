/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lemonpay.lemonpayvas.electricity.ikedc.dto.response;

/**
 *
 * @author ugochukwu.omeje
 */
public class AuthResDataDto {
    
    private String responseCode;
    private String message;
    private String apiToken;
    private String expiration;
    private Long creatTime;
    private String apiUser;

    public AuthResDataDto(String responseCode, String message, String apiToken, String expiration, Long creatTime, String apiUser) {
        this.responseCode = responseCode;
        this.message = message;
        this.apiToken = apiToken;
        this.expiration = expiration;
        this.creatTime = creatTime;
        this.apiUser = apiUser;
    }

    public AuthResDataDto(){
        
    }
    
    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public Long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Long creatTime) {
        this.creatTime = creatTime;
    }

    public String getApiUser() {
        return apiUser;
    }

    public void setApiUser(String apiUser) {
        this.apiUser = apiUser;
    }

   
    
}
