package com.controlegastos.investimentosservice.exception;

public class JwtValidationException extends RuntimeException{
    public JwtValidationException(String message){
        super(message);
    }
}
