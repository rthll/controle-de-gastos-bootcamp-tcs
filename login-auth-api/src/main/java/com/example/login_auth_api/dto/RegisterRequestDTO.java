package com.example.login_auth_api.dto;

import com.example.login_auth_api.domain.User.UserRole;

public record RegisterRequestDTO (String name, String email, String password, UserRole role) {

    public RegisterRequestDTO(String name, String email, String password) {
        this(name, email, password, UserRole.PESSOAL);
    }
}