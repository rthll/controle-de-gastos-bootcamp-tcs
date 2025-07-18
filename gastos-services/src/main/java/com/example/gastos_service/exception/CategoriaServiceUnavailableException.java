package com.example.gastos_service.exception;

public class CategoriaServiceUnavailableException extends RuntimeException {
    public CategoriaServiceUnavailableException(String message) {
        super(message);
    }

    public CategoriaServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
