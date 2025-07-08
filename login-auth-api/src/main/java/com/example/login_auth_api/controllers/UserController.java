package com.example.login_auth_api.controllers;

import com.example.login_auth_api.domain.User.User;
import com.example.login_auth_api.dto.EditRequestDTO;
import com.example.login_auth_api.dto.LoginRequestDTO;
import com.example.login_auth_api.dto.ResponseDTO;
import com.example.login_auth_api.repositories.UserRepository;
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
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<String> getUser(){
        return ResponseEntity.ok("sucesso!");
    }

    @PatchMapping
    public ResponseEntity<?> edit(@RequestBody EditRequestDTO body) {
        System.out.println(body);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String email = null;

        if (principal instanceof String) {
            email = (String) principal;
        } else {
            try {
                email = (String) principal.getClass().getMethod("getEmail").invoke(principal);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Email obtido: " + email);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado corretamente.");
        }

        Optional<User> optionalUser = repository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        User user = optionalUser.get();
        boolean updated = false;

        if (body.name() != null && !body.name().isBlank() && !body.name().equals(user.getName())) {
            user.setName(body.name());
            updated = true;
        }

        if (body.oldPassword() != null && body.newPassword() != null && !body.oldPassword().isBlank() && !body.newPassword().isBlank()) {

            if (!passwordEncoder.matches(body.oldPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Senha antiga incorreta.");
            }

            if (body.oldPassword().equals(body.newPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A nova senha não pode ser igual à antiga.");
            }

            user.setPassword(passwordEncoder.encode(body.newPassword()));
            updated = true;
        }

        if (updated) {
            repository.save(user);
            System.out.println("User atualizado com sucesso!");
            return ResponseEntity.ok("Dados atualizados com sucesso.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhum dado para atualizar.");
    }

}
