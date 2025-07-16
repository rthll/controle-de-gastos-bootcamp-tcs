package com.example.setor_service.exception;

import com.example.setor_service.dto.ErroDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
    @ExceptionHandler(SetorMembershipException.class)
    public ResponseEntity<ErroDTO> handleCategoriaMembershipException(SetorMembershipException ex) {
        ErroDTO erro = new ErroDTO("SETOR_COM_FUNCIONARIOS", ex.getMessage());
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SetorNotFoundException.class)
    public ResponseEntity<ErroDTO> handleCategoriaNotFoundException(SetorNotFoundException ex) {
        ErroDTO erro = new ErroDTO("SETOR_COM_FUNCIONARIOS", ex.getMessage());
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SetorNotBelongToUser.class)
    public ResponseEntity<ErroDTO> handleCategoriaNotBelongtoUser(SetorNotBelongToUser ex) {
        ErroDTO erro = new ErroDTO("SETOR_COM_FUNCIONARIOS", ex.getMessage());
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }
}
