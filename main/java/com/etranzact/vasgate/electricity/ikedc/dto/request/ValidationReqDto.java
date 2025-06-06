/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lemonpay.lemonpayvas.electricity.ikedc.dto.request;

/**
 *
 * @author ugochukwu.omeje
 */
public class ValidationReqDto {
 
  private String type;
  private String requestNumber;
  
  public ValidationReqDto(){
      
  }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

 
  
  
}
