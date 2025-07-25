package com.example.login_auth_api.controllers;

import com.example.login_auth_api.domain.User.User;
import com.example.login_auth_api.domain.User.UserRole;
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

    public record UserDTO(Long id, String name, String email, UserRole role) {}
    public record PasswordUpdateRequest(String email, String newPassword) {}

    @GetMapping("/user/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        Optional<User> user = repository.findByEmailWithProcedure(email);
        if (user.isPresent()) {
            User u = user.get();
            UserDTO userDTO = new UserDTO(u.getId(), u.getName(), u.getEmail(), u.getRole());
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user/password")
    public ResponseEntity<Void> updateUserPassword(@RequestBody PasswordUpdateRequest request) {
        Boolean userExists = repository.userExistsWithProcedure(request.email());
        if (userExists) {
            repository.updatePasswordWithProcedure(
                    request.email(),
                    passwordEncoder.encode(request.newPassword())
            );
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/role/{email}")
    public ResponseEntity<UserRole> getUserRole(@PathVariable String email) {
        String roleString = repository.getUserRoleWithProcedure(email);
        if (roleString != null) {
            UserRole role = UserRole.valueOf(roleString);
            return ResponseEntity.ok(role);
        }
        return ResponseEntity.notFound().build();
    }
}