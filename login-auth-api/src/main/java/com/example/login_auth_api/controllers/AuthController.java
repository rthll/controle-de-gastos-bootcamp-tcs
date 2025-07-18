package com.example.login_auth_api.controllers;

import com.example.login_auth_api.domain.User.User;
import com.example.login_auth_api.domain.User.UserRole;
import com.example.login_auth_api.dto.LoginRequestDTO;
import com.example.login_auth_api.dto.RegisterRequestDTO;
import com.example.login_auth_api.dto.ResponseDTO;
import com.example.login_auth_api.exception.UsuarioNotFoundException;
import com.example.login_auth_api.infra.security.TokenService;
import com.example.login_auth_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public record PasswordUpdateRequest(String email, String newPassword) {}

    public record UserDTO(String id, String name, String email, UserRole role) {}

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        User user = this.repository.findByEmailWithProcedure(body.email())
                .orElseThrow(() -> new UsuarioNotFoundException("usuario não encontrado"));

        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getName(), token, user.getRole()));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
        Boolean userExists = this.repository.userExistsWithProcedure(body.email());

        if(!userExists) {
            String role = body.role() != null ? body.role().name() : UserRole.PESSOAL.name();
            Long userId = this.repository.registerUserWithProcedure(
                    body.name(),
                    body.email(),
                    passwordEncoder.encode(body.password()),
                    role
            );

            User newUser = this.repository.findByEmailWithProcedure(body.email())
                    .orElseThrow(() -> new RuntimeException("Erro ao buscar usuário recém-criado"));

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token, newUser.getRole()));
        }
        return ResponseEntity.badRequest().build();
    }
}