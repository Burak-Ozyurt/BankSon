package com.sau.bankproject.DTO;

public class Account {
    private int id;
    private String branch;
    private double balance;

    // Boş Constructor
    public Account() {
    }

    // Parametreli Constructor
    public Account(int id, String branch, double balance) {
        this.id = id;
        this.branch = branch;
        this.balance = balance;
    }

    // Getter ve Setter Metotları
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // toString Metodu 
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", branch='" + branch + '\'' +
                ", balance=" + balance +
                '}';
    }
}