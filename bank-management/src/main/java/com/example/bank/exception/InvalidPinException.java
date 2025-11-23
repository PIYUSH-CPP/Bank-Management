package com.example.bank.exception;

public class InvalidPinException extends RuntimeException {
    public InvalidPinException(String msg) { super(msg); }
}
