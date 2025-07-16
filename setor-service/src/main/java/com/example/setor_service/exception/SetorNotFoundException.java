package com.example.setor_service.exception;

public class SetorNotFoundException extends RuntimeException{
    public SetorNotFoundException(String message){
        super(message);
    }
}
