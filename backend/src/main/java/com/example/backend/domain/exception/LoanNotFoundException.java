package com.example.backend.domain.exception;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(String message) {
        super(message);
    }
}
