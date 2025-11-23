package com.example.bank.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Account {
    private final String accountId;
    private final String accountNumber;
    private final Customer owner;
    private double balance;
    private String pinHash;
    private final LocalDateTime createdAt;
    private final List<Transaction> transactions = new ArrayList<>();
    private boolean closed = false;

    public Account(Customer owner, String accountNumber, String pin) {
        this.accountId = UUID.randomUUID().toString();
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.pinHash = pin;
        this.balance = 0.0;
        this.createdAt = LocalDateTime.now();
    }

    public String getAccountId() { return accountId; }
    public String getAccountNumber() { return accountNumber; }
    public Customer getOwner() { return owner; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getPinHash() { return pinHash; }
    public void setPinHash(String pinHash) { this.pinHash = pinHash; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<Transaction> getTransactions() { return transactions; }
    public boolean isClosed() { return closed; }
    public void setClosed(boolean closed) { this.closed = closed; }

    public void addTransaction(Transaction tx) {
        transactions.add(0, tx);
    }

    public synchronized void credit(double amount, Transaction tx) {
        if (closed) throw new IllegalStateException("Account is closed");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        this.balance += amount;
        addTransaction(tx);
    }

    public synchronized void debit(double amount, Transaction tx) {
        if (closed) throw new IllegalStateException("Account is closed");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (this.balance < amount) throw new IllegalArgumentException("Insufficient funds");
        this.balance -= amount;
        addTransaction(tx);
    }
}
