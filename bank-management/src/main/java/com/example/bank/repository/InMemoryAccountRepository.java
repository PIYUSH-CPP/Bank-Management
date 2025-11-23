package com.example.bank.repository;

import com.example.bank.model.Account;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountRepository {
    private final Map<String, Account> byId = new ConcurrentHashMap<>();
    private final Map<String, Account> byNumber = new ConcurrentHashMap<>();

    public void save(Account account) {
        byId.put(account.getAccountId(), account);
        byNumber.put(account.getAccountNumber(), account);
    }

    public Optional<Account> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    public Optional<Account> findByAccountNumber(String acctNo) {
        return Optional.ofNullable(byNumber.get(acctNo));
    }

    public void delete(String id) {
        var acc = byId.remove(id);
        if (acc != null) {
            byNumber.remove(acc.getAccountNumber());
        }
    }

    public List<Account> findAll() {
        return new ArrayList<>(byId.values());
    }
}
