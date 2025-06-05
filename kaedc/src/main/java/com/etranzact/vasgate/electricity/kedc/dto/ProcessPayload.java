package com.etranzact.vasgate.electricity.kedc.dto;

public class ProcessPayload {
    private Transaction transaction;

    public ProcessPayload() {
    }

    public ProcessPayload(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public String toString() {
        return "ProcessPayload{" +
                "transaction=" + transaction +
                '}';
    }
}
