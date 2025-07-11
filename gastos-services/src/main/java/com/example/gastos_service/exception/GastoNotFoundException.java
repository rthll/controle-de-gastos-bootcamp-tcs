package com.example.gastos_service.exception;

public class GastoNotFoundException extends RuntimeException {
  public GastoNotFoundException(String message) {
    super(message);
  }
}
