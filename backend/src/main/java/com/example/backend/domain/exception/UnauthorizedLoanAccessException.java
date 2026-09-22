package com.example.backend.domain.exception;

public class UnauthorizedLoanAccessException extends RuntimeException {
    public UnauthorizedLoanAccessException(String message) {
        super(message);
    }
}
