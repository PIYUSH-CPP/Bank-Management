package com.example.bank.service;

import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.InsufficientFundsException;
import com.example.bank.exception.InvalidPinException;
import com.example.bank.model.Account;
import com.example.bank.model.Customer;
import com.example.bank.model.Transaction;
import com.example.bank.model.TransactionType;
import com.example.bank.repository.InMemoryAccountRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AccountService {
    private final InMemoryAccountRepository repo;
    private final TransactionService txService;

    public AccountService(InMemoryAccountRepository repo, TransactionService txService) {
        this.repo = repo;
        this.txService = txService;
    }

    public Account openAccount(String name, String email, String phone, String initialPin, double initialDeposit) {
        Customer cust = new Customer(UUID.randomUUID().toString(), name, email, phone);
        String acctNo = generateAccountNumber();
        Account acc = new Account(cust, acctNo, initialPin);
        if (initialDeposit > 0) {
            Transaction t = new Transaction(TransactionType.OPEN_ACCOUNT, initialDeposit, "Initial deposit");
            acc.credit(initialDeposit, t);
            txService.record(t);
        }
        repo.save(acc);
        return acc;
    }

    private String generateAccountNumber() {
        String raw = String.valueOf(Math.abs(UUID.randomUUID().getMostSignificantBits()));
        if (raw.length() < 12) raw = raw + "000000000000";
        return raw.substring(0, 12);
    }

    public void deposit(String accountNumber, double amount, String description) {
        var acc = repo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive");
        Transaction tx = new Transaction(TransactionType.DEPOSIT, amount, description);
        acc.credit(amount, tx);
        txService.record(tx);
    }

    public void withdraw(String accountNumber, double amount, String pin, String description) {
        var acc = repo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
        if (!verifyPin(acc, pin)) throw new InvalidPinException("Invalid PIN");
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive");
        if (acc.getBalance() < amount) throw new InsufficientFundsException("Not enough balance");
        Transaction tx = new Transaction(TransactionType.WITHDRAWAL, amount, description);
        acc.debit(amount, tx);
        txService.record(tx);
    }

    public void transfer(String fromAccount, String toAccount, double amount, String pin, String description) {
        if (fromAccount.equals(toAccount)) throw new IllegalArgumentException("Cannot transfer to same account");
        var from = repo.findByAccountNumber(fromAccount)
                .orElseThrow(() -> new AccountNotFoundException("From account not found: " + fromAccount));
        var to = repo.findByAccountNumber(toAccount)
                .orElseThrow(() -> new AccountNotFoundException("To account not found: " + toAccount));
        if (!verifyPin(from, pin)) throw new InvalidPinException("Invalid PIN");
        if (amount <= 0) throw new IllegalArgumentException("Transfer amount must be positive");
        synchronized (this) {
            if (from.getBalance() < amount) throw new InsufficientFundsException("Not enough balance");
            Transaction txOut = new Transaction(TransactionType.TRANSFER, amount, description, toAccount);
            from.debit(amount, txOut);
            txService.record(txOut);
            Transaction txIn = new Transaction(TransactionType.DEPOSIT, amount, "Transfer from " + fromAccount, fromAccount);
            to.credit(amount, txIn);
            txService.record(txIn);
        }
    }

    public List<Transaction> getMiniStatement(String accountNumber, int maxEntries) {
        var acc = repo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
        return acc.getTransactions().stream().limit(maxEntries).collect(Collectors.toList());
    }

    public void changePin(String accountNumber, String oldPin, String newPin) {
        var acc = repo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
        if (!verifyPin(acc, oldPin)) throw new InvalidPinException("Invalid current PIN");
        acc.setPinHash(newPin);
        Transaction tx = new Transaction(TransactionType.PIN_CHANGE, 0.0, "PIN changed");
        acc.addTransaction(tx);
        txService.record(tx);
    }

    public Account getAccount(String accountNumber) {
        return repo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
    }

    public void closeAccount(String accountNumber, String pin) {
        var acc = getAccount(accountNumber);
        if (!verifyPin(acc, pin)) throw new InvalidPinException("Invalid PIN");
        acc.setClosed(true);
        repo.save(acc);
        Transaction tx = new Transaction(TransactionType.WITHDRAWAL, 0.0, "Account closed");
        acc.addTransaction(tx);
        txService.record(tx);
    }

    private boolean verifyPin(Account acc, String pin) {
        if (pin == null) return false;
        return pin.equals(acc.getPinHash());
    }
}
