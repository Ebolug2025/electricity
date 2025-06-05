package com.etranzact.vasgate.electricity.kedc.dto;

import java.util.List;

public class Transaction {
    private long id;
    private String customer_name;
    private String transaction_reference;
    private double previous_arrears;
    private double current_arrears;
    private String service_band;
    private String region;
    private Double amount;
    private Double units_count;
    private Double cost_of_units;
    private double meter_number;
    private double account_number;
    private String account_type;
    private String transaction_status;
    private String payment_method;
    private String tariff_code;
    private String tariff_rate;
    private Double vat;
    String deductions;
    private String csp;
    private String transaction_date;
    private String vendor;
    private List<GeneratedToken> tokens;

    public Transaction() {
    }

    public Transaction(long id, String customer_name, String transaction_reference,String deductions, double previous_arrears, double current_arrears, Double amount, Double units_count, Double cost_of_units, double meter_number, double account_number, String account_type, String transaction_status, String payment_method, String tariff_code, String tariff_rate, Double vat, String transaction_date, String vendor, String region, String service_band, String  csp, List<GeneratedToken> tokens) {
        this.id = id;
        this.customer_name = customer_name;
        this.transaction_reference = transaction_reference;
        this.previous_arrears = previous_arrears;
        this.current_arrears = current_arrears;
        this.amount = amount;
        this.units_count = units_count;
        this.cost_of_units = cost_of_units;
        this.meter_number = meter_number;
        this.account_number = account_number;
        this.account_type = account_type;
        this.transaction_status = transaction_status;
        this.payment_method = payment_method;
        this.tariff_code = tariff_code;
        this.tariff_rate = tariff_rate;
        this.vat = vat;
        this.transaction_date = transaction_date;
        this.vendor = vendor;
        this.tokens = tokens;
        this.region = region;
        this.service_band = service_band;
        this.csp = csp;
        this.deductions = deductions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getTransaction_reference() {
        return transaction_reference;
    }

    public void setTransaction_reference(String transaction_reference) {
        this.transaction_reference = transaction_reference;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCsp() {
        return csp;
    }

    public void setCsp(String csp) {
        this.csp = csp;
    }

    public String getDeductions() {
        return deductions;
    }

    public void setDeductions(String deductions) {
        this.deductions = deductions;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getUnits_count() {
        return units_count;
    }

    public void setUnits_count(Double units_count) {
        this.units_count = units_count;
    }

    public double getMeter_number() {
        return meter_number;
    }

    public void setMeter_number(double meter_number) {
        this.meter_number = meter_number;
    }

    public double getAccount_number() {
        return account_number;
    }

    public void setAccount_number(double account_number) {
        this.account_number = account_number;
    }

    public String getAccount_type() {
        return account_type;
    }

    public String getService_band() {
        return service_band;
    }

    public void setService_band(String service_band) {
        this.service_band = service_band;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getTransaction_status() {
        return transaction_status;
    }

    public void setTransaction_status(String transaction_status) {
        this.transaction_status = transaction_status;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getTariff_code() {
        return tariff_code;
    }

    public void setTariff_code(String tariff_code) {
        this.tariff_code = tariff_code;
    }

    public String getTariff_rate() {
        return tariff_rate;
    }

    public void setTariff_rate(String tariff_rate) {
        this.tariff_rate = tariff_rate;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public double getPrevious_arrears() {
        return previous_arrears;
    }

    public void setPrevious_arrears(double previous_arrears) {
        this.previous_arrears = previous_arrears;
    }

    public Double getCost_of_units() {
        return cost_of_units;
    }

    public List<GeneratedToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<GeneratedToken> tokens) {
        this.tokens = tokens;
    }

    public void setCost_of_units(Double cost_of_units) {
        this.cost_of_units = cost_of_units;
    }

    public double getCurrent_arrears() {
        return current_arrears;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCurrent_arrears(double current_arrears) {
        this.current_arrears = current_arrears;
    }

    public String getVendor() {
        return vendor;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", customer_name='" + customer_name + '\'' +
                ", transaction_reference='" + transaction_reference + '\'' +
                ", amount=" + amount +
                ", number_of_units=" + units_count +
                ", meter_number=" + meter_number +
                ", account_number=" + account_number +
                ", account_type='" + account_type + '\'' +
                ", transaction_status='" + transaction_status + '\'' +
                ", payment_method='" + payment_method + '\'' +
                ", tariff_code='" + tariff_code + '\'' +
                ", tariff_rate=" + tariff_rate +
                ", transaction_date=" + transaction_date +
                ", vendor='" + vendor + '\'' +
                ", generated_token='" + tokens + '\'' +
                '}';
    }


}
