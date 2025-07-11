package com.example.gastos_service.exception;

public class CategoriaNotFoundException extends RuntimeException {
    public CategoriaNotFoundException(String message) {
        super(message);
    }
}
