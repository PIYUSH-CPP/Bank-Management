package com.example.bank;

import com.example.bank.repository.InMemoryAccountRepository;
import com.example.bank.service.AccountService;
import com.example.bank.service.TransactionService;

public class App {
    public static void main(String[] args) {
        InMemoryAccountRepository repo = new InMemoryAccountRepository();
        TransactionService txService = new TransactionService();
        AccountService accountService = new AccountService(repo, txService);

        var acc = accountService.openAccount("Piyush Kumar", "piyush@example.com", "9999999999", "1234", 1000.0);
        System.out.println("Opened account: " + acc.getAccountNumber() + " balance=" + acc.getBalance());

        accountService.deposit(acc.getAccountNumber(), 500.0, "Salary");
        System.out.println("After deposit balance=" + accountService.getAccount(acc.getAccountNumber()).getBalance());

        accountService.withdraw(acc.getAccountNumber(), 200.0, "1234", "ATM withdrawal");
        System.out.println("After withdrawal balance=" + accountService.getAccount(acc.getAccountNumber()).getBalance());

        var mini = accountService.getMiniStatement(acc.getAccountNumber(), 5);
        System.out.println("Mini statement (latest):");
        mini.forEach(t -> System.out.println(t));
    }
}
