package com.example.login_auth_api.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message){
        super(message);
    }
}
