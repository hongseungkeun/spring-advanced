package org.example.expert.domain.common.exception;

public class AdminAuthenticationFailedException extends RuntimeException {

    public AdminAuthenticationFailedException(String message) {
        super(message);
    }
}