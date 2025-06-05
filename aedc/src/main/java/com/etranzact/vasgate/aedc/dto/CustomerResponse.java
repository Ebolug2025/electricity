package com.etranzact.vasgate.aedc.dto;

import com.etranzact.vasgate.aedc.model.Customer;

import java.util.List;

public class CustomerResponse
{
    private List<Customer> customers;

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    @Override
    public String toString() {
        return "CustomerResponse{" +
                "customers=" + customers +
                '}';
    }
}
