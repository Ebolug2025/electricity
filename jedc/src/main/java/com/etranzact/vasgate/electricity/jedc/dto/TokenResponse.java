package com.etranzact.vasgate.electricity.jedc.dto;

public class TokenResponse
{
    private String token;
    private String username;
    private String expires;
    private String issued;
    private String error;
    private String description;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TokenResponse{" + "token=" + token + ", username=" + username + ", expires=" + expires + ", issued=" + issued + ", error=" + error + ", description=" + description + '}';
    }

    public TokenResponse(String token, String username, String expires, String issued, String error, String description) {
        this.token = token;
        this.username = username;
        this.expires = expires;
        this.issued = issued;
        this.error = error;
        this.description = description;
    }




}
