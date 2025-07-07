package com.example.recuperar_senha_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${password.reset.frontend.url}")
    private String frontendUrl;

    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Recuperação de Senha");

            String resetUrl = frontendUrl + "?token=" + token;
            String emailBody = String.format(
                    "Olá,\n\n" +
                            "Você solicitou a recuperação de senha.\n" +
                            "Clique no link abaixo para redefinir sua senha:\n\n" +
                            "%s\n\n" +
                            "Este link expira em 1 hora.\n\n" +
                            "Se você não solicitou esta recuperação, ignore este email.\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe de Suporte",
                    resetUrl
            );

            message.setText(emailBody);
            mailSender.send(message);

            log.info("Email de recuperação enviado para: {}", toEmail);
        } catch (Exception e) {
            log.error("Erro ao enviar email para {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Erro ao enviar email de recuperação", e);
        }
    }
}