package com.example.gastos_service.exception;

import com.example.gastos_service.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CategoriaServiceUnavailableException.class)
    public ResponseEntity<ErrorDTO> handleCategoriaServiceUnavailable(CategoriaServiceUnavailableException ex) {
        log.error("Serviço de categorias indisponível: {}", ex.getMessage());

        ErrorDTO error = new ErrorDTO(
                "CATEGORIA_SERVICE_UNAVAILABLE",
                "Serviço de categorias temporariamente indisponível. Tente novamente em alguns instantes."
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(CategoriaNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleCategoriaNotFound(CategoriaNotFoundException ex) {
        log.error("Categoria não encontrada: {}", ex.getMessage());

        ErrorDTO error = new ErrorDTO(
                "CATEGORIA_NOT_FOUND",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}