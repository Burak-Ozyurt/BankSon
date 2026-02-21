package com.sau.bankproject.DTO;

public class Depositor {
    private int customerId;
    private int accountId;

    public Depositor() {}

    public Depositor(int customerId, int accountId) {
        this.customerId = customerId;
        this.accountId = accountId;
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }
}