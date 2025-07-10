package com.example.login_auth_api.exception;

import com.example.login_auth_api.dto.ErroDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ErroDTO> handleUsuarioNotFoundException(UsuarioNotFoundException ex) {
        ErroDTO erro = new ErroDTO("USUARIO_NOT_EXISTS", ex.getMessage());
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }
}
