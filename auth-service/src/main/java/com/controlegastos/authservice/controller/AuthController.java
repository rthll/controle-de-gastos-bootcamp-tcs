package com.controlegastos.authservice.controller;

import com.controlegastos.authservice.dto.*;
import com.controlegastos.authservice.entity.Role;
import com.controlegastos.authservice.entity.Usuario;
import com.controlegastos.authservice.service.JwtService;
import com.controlegastos.authservice.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        Usuario novoUsuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .role(Role.USER)
                .build();

        usuarioService.salvar(novoUsuario);

        String token = jwtService.generateToken(novoUsuario.getEmail());

        return ResponseEntity.ok(AuthResponse.builder().token(token).build());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()
                )
        );

        UserDetails user = usuarioService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(user.getUsername());
        Usuario usuario = usuarioService.searchUser(request.getEmail());
        String nome = usuario.getNome();
        return ResponseEntity.ok(AuthResponse.builder().token(token).build());
    }
}
