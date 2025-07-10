package com.example.categorias_services.exception;

public class CategoriaNotBelongToUser extends RuntimeException{
    public CategoriaNotBelongToUser(String message){
        super(message);
    }
}
