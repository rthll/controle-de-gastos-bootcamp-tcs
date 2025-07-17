package com.example.login_auth_api.domain.User;

public enum UserRole {
    PESSOAL("ROLE_PESSOAL"),
    EMPRESARIAL("ROLE_EMPRESARIAL");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}