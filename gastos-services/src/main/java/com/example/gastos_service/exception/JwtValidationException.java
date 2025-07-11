package com.example.gastos_service.exception;

public class JwtValidationException extends RuntimeException{
    public JwtValidationException(String message){
        super(message);
    }
}
