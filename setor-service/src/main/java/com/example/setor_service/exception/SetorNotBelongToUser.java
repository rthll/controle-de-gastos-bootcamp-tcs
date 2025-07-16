package com.example.setor_service.exception;

public class SetorNotBelongToUser extends RuntimeException {
    public SetorNotBelongToUser(String message){
        super(message);
    }
}
