/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.vasgate.electricity.jedc.dto;

/**
 *
 * @author cindy.eke
 */
public class QueryResponse
{
    private Customer customer;
    private String status;
    private String accessCode;
    private String message;
    private String time;

    public QueryResponse(Customer customer, String status, String accessCode, String message, String time) {
        this.customer = customer;
        this.status = status;
        this.accessCode = accessCode;
        this.message = message;
        this.time = time;
    }

    public QueryResponse() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "QueryResponse{" + "customer=" + customer + ", status=" + status + ", accessCode=" + accessCode + ", message=" + message + ", time=" + time + '}';
    }

    
    
}
