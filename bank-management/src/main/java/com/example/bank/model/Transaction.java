package com.example.bank.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private final String id;
    private final TransactionType type;
    private final double amount;
    private final LocalDateTime timestamp;
    private final String description;
    private final String relatedAccount;

    public Transaction(TransactionType type, double amount, String description) {
        this(type, amount, description, null);
    }

    public Transaction(TransactionType type, double amount, String description, String relatedAccount) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.relatedAccount = relatedAccount;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() { return id; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
    public String getRelatedAccount() { return relatedAccount; }

    @Override
    public String toString() {
        return timestamp + " " + type + " " + amount + " " + description + (relatedAccount != null ? " -> " + relatedAccount : "");
    }
}
