package com.example.recuperar_senha_service.service;

import com.example.recuperar_senha_service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceClient {

    private final RestTemplate restTemplate;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    public UserDTO getUserByEmail(String email) {
        try {
            // CORREÇÃO: Usar o método correto do RestTemplate com placeholder
            String url = authServiceUrl + "/internal/user/email/{email}";
            ResponseEntity<UserDTO> response = restTemplate.getForEntity(url, UserDTO.class, email);
            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao buscar usuário por email {}: {}", email, e.getMessage());
            return null;
        }
    }

    public boolean updateUserPassword(String email, String newPassword) {
        try {
            String url = authServiceUrl + "/internal/user/password";
            var request = new PasswordUpdateRequest(email, newPassword);
            ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Erro ao atualizar senha do usuário {}: {}", email, e.getMessage());
            return false;
        }
    }

    private record PasswordUpdateRequest(String email, String newPassword) {}
}