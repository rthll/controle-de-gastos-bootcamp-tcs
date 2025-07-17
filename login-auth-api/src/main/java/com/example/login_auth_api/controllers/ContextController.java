package com.example.login_auth_api.controllers;

import com.example.login_auth_api.domain.User.User;
import com.example.login_auth_api.domain.User.UserRole;
import com.example.login_auth_api.dto.ResponseDTO;
import com.example.login_auth_api.infra.security.TokenService;
import com.example.login_auth_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/context")
@RequiredArgsConstructor
public class ContextController {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public record ContextSwitchRequest(UserRole newRole) {}

    @PostMapping("/switch")
    public ResponseEntity<?> switchContext(@RequestBody ContextSwitchRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        User user = (User) authentication.getPrincipal();

        user.setRole(request.newRole());
        userRepository.save(user);

        String newToken = tokenService.generateToken(user);

        return ResponseEntity.ok(new ResponseDTO(user.getName(), newToken, user.getRole()));
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(new ContextResponse(user.getRole()));
    }

    public record ContextResponse(UserRole currentRole) {}
}