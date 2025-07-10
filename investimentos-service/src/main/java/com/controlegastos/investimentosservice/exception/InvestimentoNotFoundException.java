package com.controlegastos.investimentosservice.exception;

public class InvestimentoNotFoundException extends RuntimeException {
    public InvestimentoNotFoundException(String message) {
        super(message);
    }
}