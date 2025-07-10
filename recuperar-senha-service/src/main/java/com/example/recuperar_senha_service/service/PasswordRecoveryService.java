package com.example.recuperar_senha_service.service;

import com.example.recuperar_senha_service.domain.PasswordResetToken;
import com.example.recuperar_senha_service.dto.UserDTO;
import com.example.recuperar_senha_service.repositories.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordRecoveryService {

    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthServiceClient authServiceClient;

    @Value("${password.reset.token.expiration}")
    private long tokenExpirationMs;

    @Transactional
    public void requestPasswordReset(String email) {
        // Verificar se usuário existe
        UserDTO user = authServiceClient.getUserByEmail(email);
        if (user == null) {
            log.warn("Tentativa de recuperação para email não existente: {}", email);
            // Por segurança, não informamos que o email não existe
            return;
        }

        tokenRepository.deleteByEmail(email);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(tokenExpirationMs / 1000);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(email);
        resetToken.setExpiryDate(expiryDate);

        tokenRepository.save(resetToken);

        // Enviar email
        emailService.sendPasswordResetEmail(email, token);

        log.info("Token de recuperação criado para: {}", email);
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> optionalToken = tokenRepository.findByTokenAndUsedFalse(token);

        if (optionalToken.isEmpty()) {
            log.warn("Token não encontrado ou já utilizado: {}", token);
            return false;
        }

        PasswordResetToken resetToken = optionalToken.get();

        // Verificar se token expirou
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.warn("Token expirado: {}", token);
            return false;
        }

        // Atualizar senha no serviço de autenticação
        boolean passwordUpdated = authServiceClient.updateUserPassword(resetToken.getEmail(), newPassword);

        if (passwordUpdated) {
            // Marcar token como usado
            resetToken.setUsed(true);
            tokenRepository.save(resetToken);

            log.info("Senha atualizada com sucesso para: {}", resetToken.getEmail());
            return true;
        }

        log.error("Erro ao atualizar senha para: {}", resetToken.getEmail());
        return false;
    }

    @Scheduled(fixedRate = 3600000) // Executa a cada hora
    @Transactional
    public void cleanupExpiredTokens() {
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());
        log.info("Tokens expirados removidos");
    }
}