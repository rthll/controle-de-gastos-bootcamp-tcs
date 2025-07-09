package com.example.categorias_services.exception;

public class CategoriaNotFoundException extends RuntimeException{
    public CategoriaNotFoundException(String message){
        super(message);
    }
}
