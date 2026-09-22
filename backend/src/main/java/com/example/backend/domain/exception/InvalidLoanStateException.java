package com.example.backend.domain.exception;

public class InvalidLoanStateException extends RuntimeException {
    public InvalidLoanStateException(String message) {
        super(message);
    }
}
