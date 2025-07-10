package com.example.recuperar_senha_service.exception;

import com.example.recuperar_senha_service.dto.ErroDTO;
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

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ErroDTO> handleMessageException(MessageException ex) {
        ErroDTO erro = new ErroDTO("MENSAGEM_ERROR", ex.getMessage());
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }
}
