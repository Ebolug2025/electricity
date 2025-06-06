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
public class EncPinReqDto {
    
    private String wallet;
    private String username;
    private String password;
    private String pin;

    public EncPinReqDto(String wallet, String username, String password, String pin) {
        this.wallet = wallet;
        this.username = username;
        this.password = password;
        this.pin = pin;
    }
    
    public EncPinReqDto(){
        
    }
    
    

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
    
    
}
