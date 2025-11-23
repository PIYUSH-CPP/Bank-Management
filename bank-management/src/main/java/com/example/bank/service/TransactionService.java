package com.example.bank.service;

import com.example.bank.model.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionService {
    private final List<Transaction> global = Collections.synchronizedList(new ArrayList<>());

    public void record(Transaction tx) {
        global.add(tx);
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(global);
    }

    public List<Transaction> findByAccount(String accountNumber, int limit) {
        return global.stream()
                .filter(tx -> accountNumber.equals(tx.getRelatedAccount()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
