package com.etranzact.vasgate.aedc.dto;

public class FailResponse {
    private String code;
    private String msgUser;
    private String msgDeveloper;

    public String getCode() {
        return code;
    }

    public FailResponse setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMsgUser() {
        return msgUser;
    }

    public FailResponse setMsgUser(String msgUser) {
        this.msgUser = msgUser;
        return this;
    }

    public String getMsgDeveloper() {
        return msgDeveloper;
    }

    public FailResponse setMsgDeveloper(String msgDeveloper) {
        this.msgDeveloper = msgDeveloper;
        return this;
    }

    @Override
    public String toString() {
        return "FailResponse{" +
                "code='" + code + '\'' +
                ", msgUser='" + msgUser + '\'' +
                ", msgDeveloper='" + msgDeveloper + '\'' +
                '}';
    }
}
