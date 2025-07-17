package com.example.login_auth_api.dto;

import com.example.login_auth_api.domain.User.UserRole;

public record ResponseDTO (String name, String token, UserRole role) { }