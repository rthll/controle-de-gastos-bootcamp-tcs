package com.controlegastos.investimentosservice.exception;

public class CompraInvestimentoNotFoundException extends RuntimeException {
    public CompraInvestimentoNotFoundException(String message) {
        super(message);
    }
}