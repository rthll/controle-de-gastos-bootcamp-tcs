package com.example.login_auth_api.controllers;

import com.example.login_auth_api.domain.User.User;
import com.example.login_auth_api.dto.EditRequestDTO;
import com.example.login_auth_api.dto.LoginRequestDTO;
import com.example.login_auth_api.dto.ResponseDTO;
import com.example.login_auth_api.exception.UsuarioNotFoundException;
import com.example.login_auth_api.repositories.UserRepository;
import com.example.login_auth_api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<String> getUser(){
        return ResponseEntity.ok("sucesso!");
    }

    @PatchMapping
    public ResponseEntity<?> edit(@RequestBody EditRequestDTO body) {
        String email = getEmailFromPrincipal();

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado corretamente.");
        }

        userService.editarUsuario(email, body);
        return ResponseEntity.ok("Dados atualizados com sucesso.");
    }

    private String getEmailFromPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof String) {
            return (String) principal;
        }

        try {
            return (String) principal.getClass().getMethod("getEmail").invoke(principal);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/verifica/{email}")
    public ResponseEntity<?> verificaCadastro(@PathVariable String email){
        Boolean userExists = userRepository.userExistsWithProcedure(email);

        if(!userExists) {
            throw new UsuarioNotFoundException("Usuário não encontrado");
        }

        return ResponseEntity.ok().build();
    }
}