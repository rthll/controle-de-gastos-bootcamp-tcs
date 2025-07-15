package com.example.login_auth_api.controllers;

import com.example.login_auth_api.domain.User.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.login_auth_api.repositories.UserRepository;
import java.util.Optional;


@RestController
@RequestMapping("/internal")
public class InternalController {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public InternalController(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public record UserDTO(Long id, String name, String email) {}
    public record PasswordUpdateRequest(String email, String newPassword) {}


    @GetMapping("/user/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        Optional<User> user = repository.findByEmail(email);
        if (user.isPresent()) {
            User u = user.get();
            UserDTO userDTO = new UserDTO(u.getId(), u.getName(), u.getEmail());
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user/password")
    public ResponseEntity<Void> updateUserPassword(@RequestBody PasswordUpdateRequest request) {
        Optional<User> user = repository.findByEmail(request.email());
        if (user.isPresent()) {
            User u = user.get();
            u.setPassword(passwordEncoder.encode(request.newPassword()));
            repository.save(u);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}