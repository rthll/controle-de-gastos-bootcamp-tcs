package com.example.login_auth_api.services;

import com.example.login_auth_api.domain.User.User;
import com.example.login_auth_api.dto.EditRequestDTO;
import com.example.login_auth_api.exception.InvalidPasswordException;
import com.example.login_auth_api.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void editarUsuario(String email, EditRequestDTO dto) throws InvalidPasswordException {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        boolean updated = false;

        if (dto.name() != null && !dto.name().isBlank() && !dto.name().equals(user.getName())) {
            user.setName(dto.name());
            updated = true;
        }

        if (dto.oldPassword() != null && dto.newPassword() != null &&
                !dto.oldPassword().isBlank() && !dto.newPassword().isBlank()) {

            if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword())) {
                throw new InvalidPasswordException("Senha antiga incorreta.");
            }

            if (dto.oldPassword().equals(dto.newPassword())) {
                throw new InvalidPasswordException("A nova senha não pode ser igual à antiga.");
            }

            user.setPassword(passwordEncoder.encode(dto.newPassword()));
            updated = true;
        }

        if (!updated) {
            throw new IllegalArgumentException("Nenhum dado para atualizar.");
        }

        repository.save(user);
    }
}
