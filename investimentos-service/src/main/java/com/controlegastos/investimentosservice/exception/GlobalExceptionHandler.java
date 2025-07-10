package com.controlegastos.investimentosservice.exception;

import com.controlegastos.investimentosservice.dto.ErroDTO;
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

    @ExceptionHandler(InvestimentoNotFoundException.class)
    public ResponseEntity<ErroDTO> handleInvestimentoNotFound(InvestimentoNotFoundException ex) {
        ErroDTO erro = new ErroDTO("INVESTIMENTOS_ERRO", ex.getMessage());
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RendaNotFoundException.class)
    public ResponseEntity<ErroDTO> handleRendaNotFound(RendaNotFoundException ex) {
        ErroDTO erro = new ErroDTO("INVESTIMENTOS_ERRO", ex.getMessage());
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CompraInvestimentoNotFoundException.class)
    public ResponseEntity<ErroDTO> handleCompraInvestimentoNotFound(CompraInvestimentoNotFoundException ex) {
        ErroDTO erro = new ErroDTO("INVESTIMENTOS_ERRO", ex.getMessage());
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErroDTO>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErroDTO> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return new ErroDTO(fieldName, errorMessage);
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroDTO> handleGenericException(Exception ex) {
        ErroDTO erro = new ErroDTO("ERRO_INTERNO", "Erro interno do servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}