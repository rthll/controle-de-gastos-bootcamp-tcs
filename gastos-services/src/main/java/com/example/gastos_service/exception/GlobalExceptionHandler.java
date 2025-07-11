package com.example.gastos_service.exception;

import com.example.gastos_service.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GastoNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleGastoNotFound(GastoNotFoundException ex) {
        ErrorDTO erro = new ErrorDTO("GASTO_ERRO", ex.getMessage());
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoriaNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleRendaNotFound(CategoriaNotFoundException ex) {
        ErrorDTO erro = new ErrorDTO("GASTO_ERRO", ex.getMessage());
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorDTO>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorDTO> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return new ErrorDTO(fieldName, errorMessage);
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGenericException(Exception ex) {
        ErrorDTO erro = new ErrorDTO("ERRO_INTERNO", "Erro interno do servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}
