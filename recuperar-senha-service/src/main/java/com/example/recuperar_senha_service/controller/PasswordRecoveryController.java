package com.example.recuperar_senha_service.controller;

import com.example.recuperar_senha_service.dto.ApiResponseDTO;
import com.example.recuperar_senha_service.dto.PasswordResetConfirmDTO;
import com.example.recuperar_senha_service.dto.PasswordResetRequestDTO;
import com.example.recuperar_senha_service.service.PasswordRecoveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password-recovery")
@RequiredArgsConstructor
@Slf4j
public class PasswordRecoveryController {

    private final PasswordRecoveryService passwordRecoveryService;

    @PostMapping("/request")
    public ResponseEntity<ApiResponseDTO> requestPasswordReset(
            @Valid @RequestBody PasswordResetRequestDTO request) {

        try {
            passwordRecoveryService.requestPasswordReset(request.email());
            return ResponseEntity.ok(
                    ApiResponseDTO.success("Se o email existir, você receberá instruções para recuperação.")
            );
        } catch (Exception e) {
            log.error("Erro ao processar solicitação de recuperação: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(
                    ApiResponseDTO.error("Erro interno do servidor")
            );
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<ApiResponseDTO> confirmPasswordReset(
            @Valid @RequestBody PasswordResetConfirmDTO request) {

        try {
            boolean success = passwordRecoveryService.resetPassword(request.token(), request.newPassword());

            if (success) {
                return ResponseEntity.ok(
                        ApiResponseDTO.success("Senha atualizada com sucesso!")
                );
            } else {
                return ResponseEntity.badRequest().body(
                        ApiResponseDTO.error("Token inválido ou expirado")
                );
            }
        } catch (Exception e) {
            log.error("Erro ao confirmar recuperação de senha: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(
                    ApiResponseDTO.error("Erro interno do servidor")
            );
        }
    }
}