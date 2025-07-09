package com.example.login_auth_api.exception;

public class UsuarioNotFoundException extends RuntimeException{
    public UsuarioNotFoundException(String message){
        super(message);
    }
}
