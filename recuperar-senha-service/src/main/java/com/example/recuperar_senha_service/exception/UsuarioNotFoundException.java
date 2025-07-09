package com.example.recuperar_senha_service.exception;

public class UsuarioNotFoundException extends RuntimeException{
    public UsuarioNotFoundException(String message){
        super(message);
    }
}
