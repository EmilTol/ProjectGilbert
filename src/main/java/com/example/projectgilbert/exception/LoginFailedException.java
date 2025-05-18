package com.example.projectgilbert.exception;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException(String username) {
        super("Login failed for username: " + username);
    }
}
