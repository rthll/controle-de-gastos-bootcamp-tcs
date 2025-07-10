package com.example.recuperar_senha_service.service;

import com.example.recuperar_senha_service.exception.MessageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

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
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Recuperação de Senha - AntBalance");

            String resetUrl = frontendUrl + "?token=" + token;

            String emailBody = String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>
                <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;">
                    <table cellpadding="0" cellspacing="0" width="100%%" style="background-color: #f4f4f4; padding: 20px 0;">
                        <tr>
                            <td align="center">
                                <table cellpadding="0" cellspacing="0" width="600" style="background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                                    <tr>
                                        <td style="padding: 30px 30px 20px 30px; text-align: center; background-color: #ffffff; border-radius: 8px 8px 0 0;">
                                            <img src="cid:logo" alt="AntBalance" style="max-width: 200px; height: auto;">
                                        </td>
                                    </tr>
                                
                                    <tr>
                                        <td style="padding: 40px 30px;">
                                            <h2 style="color: #333333; font-size: 24px; margin: 0 0 20px 0;">Olá!</h2>
                                            
                                            <p style="color: #666666; font-size: 16px; line-height: 1.6; margin: 0 0 30px 0;">
                                                Você solicitou a recuperação de senha.<br>
                                                Clique no botão abaixo para redefinir sua senha:
                                            </p>
                                            
                                            <table cellpadding="0" cellspacing="0" width="100%%">
                                                <tr>
                                                    <td align="center" style="padding: 20px 0;">
                                                        <a href="%s" style="background-color: #1a45b8; color: #ffffff; padding: 12px 30px; text-decoration: none; border-radius: 5px; font-size: 16px; font-weight: bold; display: inline-block;">
                                                            Redefinir Senha
                                                        </a>
                                                    </td>
                                                </tr>
                                            </table>
                                            
                                            <p style="color: #999999; font-size: 14px; line-height: 1.6; margin: 20px 0; text-align: center;">
                                                Ou copie e cole este link no seu navegador:<br>
                                                <a href="%s" style="color: #1a45b8; text-decoration: none; word-break: break-all;">%s</a>
                                            </p>
                                            <p style="color: #000000; font-size: 14px; line-height: 1.6; margin: 30px 0 0 0;">
                                                Se você não solicitou esta recuperação, ignore este email.
                                            </p>
                                            
                                            <hr style="border: none; border-top: 1px solid #eeeeee; margin: 30px 0;">
                                            
                                            <p style="color: #666666; font-size: 14px; margin: 0;">
                                                Atenciosamente,<br>
                                                <strong>AntBalance App</strong>
                                            </p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """, resetUrl, resetUrl, resetUrl);
            // Template HTML estilizado

            helper.setText(emailBody, true); // true = HTML

            ClassPathResource logoResource = new ClassPathResource("static/images/logomini.png");
            helper.addInline("logo", logoResource);

            mailSender.send(mimeMessage);
            log.info("Email de recuperação enviado para: {}", toEmail);

        } catch (MessagingException e) {
            log.error("Erro ao enviar email para {}: {}", toEmail, e.getMessage());
            throw new MessageException("Erro ao enviar email de recuperação");
        }
    }
}