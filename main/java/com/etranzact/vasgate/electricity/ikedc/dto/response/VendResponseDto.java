package com.lemonpay.lemonpayvas.electricity.ikedc.dto.response;

public class VendResponseDto {

    private Boolean status;
    private String message;
    private int responseCode;
    private VendDataDto data;

    private String demandCategory;
    private String assetProvider;

    public VendResponseDto() {
    }

    public VendResponseDto(Boolean status, String message, int responseCode, VendDataDto data, String demandCategory, String assetProvider) {
        this.status = status;
        this.message = message;
        this.responseCode = responseCode;
        this.data = data;
        this.demandCategory = demandCategory;
        this.assetProvider = assetProvider;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public VendDataDto getData() {
        return data;
    }

    public void setData(VendDataDto data) {
        this.data = data;
    }

    public String getDemandCategory() {
        return demandCategory;
    }

    public void setDemandCategory(String demandCategory) {
        this.demandCategory = demandCategory;
    }

    public String getAssetProvider() {
        return assetProvider;
    }

    public void setAssetProvider(String assetProvider) {
        this.assetProvider = assetProvider;
    }
}
