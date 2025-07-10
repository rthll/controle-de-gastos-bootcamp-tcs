package com.example.categorias_services.exception;

import com.example.categorias_services.dto.ErroDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CategoriaMembershipException.class)
    public ResponseEntity<ErroDTO> handleCategoriaMembershipException(CategoriaMembershipException ex) {
        ErroDTO erro = new ErroDTO("CATEGORIA_COM_GASTOS", ex.getMessage());
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoriaNotFoundException.class)
    public ResponseEntity<ErroDTO> handleCategoriaNotFoundException(CategoriaNotFoundException ex) {
        ErroDTO erro = new ErroDTO("CATEGORIA_COM_GASTOS", ex.getMessage());
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoriaNotBelongToUser.class)
    public ResponseEntity<ErroDTO> handleCategoriaNotBelongtoUser(CategoriaNotBelongToUser ex) {
        ErroDTO erro = new ErroDTO("CATEGORIA_COM_GASTOS", ex.getMessage());
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }


}
