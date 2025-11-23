📌 Bank Management System – Java Backend (Maven Project)

A pure Java backend for a simple banking system.
This project demonstrates core banking operations such as:

Opening an account

Deposits & withdrawals

Transferring money between accounts

Generating mini statements

Changing PIN

Managing customers

Basic transaction logging


This project uses Java 21 and Maven, and follows a clean backend architecture with services, models, and repositories.

🚀 Features

✔ Account Management

Create new bank accounts

Generate unique account numbers

Change PIN

Close account


✔ Transactions

Deposit

Withdraw

Transfer between accounts

View mini statement (latest N transactions)


✔ Transaction Logging

Every action generates a Transaction record

Stored per-account + globally


✔ Clean Architecture (No frameworks)

Pure Java, no Spring Boot (can be added later).
Repository is in-memory for simplicity.
