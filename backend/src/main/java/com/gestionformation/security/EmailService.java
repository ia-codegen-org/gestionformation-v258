package com.gestionformation.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail-from}")
    private String mailFrom;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public void sendCredentialsEmail(String toEmail, String username,
                                      String password, String role) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailFrom);
            helper.setTo(toEmail);
            helper.setSubject("Vos identifiants de connexion");
            helper.setText(buildCredentialsHtml(username, password, role, frontendUrl), true);
            mailSender.send(message);
            log.info("✅ Email d'identifiants envoyé à {}", toEmail);
        } catch (MailAuthenticationException e) {
            log.error("❌ Authentification SMTP échouée pour l'envoi à {} — vérifiez spring.mail.username / spring.mail.password (mot de passe d'application requis pour Gmail)", toEmail, e);
        } catch (MailException e) {
            log.error("❌ Erreur d'envoi de l'email d'identifiants à {}", toEmail, e);
        } catch (Exception e) {
            log.error("❌ Erreur envoi email identifiants à {}", toEmail, e);
        }
    }

    public void sendPasswordResetEmail(String toEmail, String resetToken, String frontendUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String resetLink = frontendUrl + "/reset-password?token=" + resetToken;
            helper.setFrom(mailFrom);
            helper.setTo(toEmail);
            helper.setSubject("Réinitialisation de votre mot de passe");
            helper.setText(buildResetHtml(resetLink), true);
            mailSender.send(message);
            log.info("✅ Email de réinitialisation envoyé à {}", toEmail);
        } catch (MailAuthenticationException e) {
            log.error("❌ Authentification SMTP échouée pour l'envoi à {} — vérifiez spring.mail.username / spring.mail.password (mot de passe d'application requis pour Gmail)", toEmail, e);
        } catch (MailException e) {
            log.error("❌ Erreur d'envoi de l'email de réinitialisation à {}", toEmail, e);
        } catch (Exception e) {
            log.error("❌ Erreur envoi email réinitialisation à {}", toEmail, e);
        }
    }

    private String buildCredentialsHtml(String username, String password, String role, String appUrl) {
        return """
            <div style='font-family: Arial, sans-serif; max-width: 480px; margin: 0 auto;'>
              <div style='background: linear-gradient(135deg, #023773, #1e4f9c); padding: 32px; border-radius: 12px 12px 0 0;'>
                <h2 style='color: white; margin: 0;'>Bienvenue !</h2>
              </div>
              <div style='background: white; padding: 32px; border: 1px solid #e2e5f0; border-top: none; border-radius: 0 0 12px 12px;'>
                <p>Votre compte a été créé avec le rôle <strong>%s</strong>.</p>
                <p>Voici vos identifiants de connexion :</p>
                <div style='background: #f8f9fc; border-radius: 8px; padding: 16px; margin: 16px 0;'>
                  <p style='margin: 4px 0;'><strong>Nom d'utilisateur :</strong> %s</p>
                  <p style='margin: 4px 0;'><strong>Mot de passe :</strong> %s</p>
                </div>
                <div style='text-align: center; margin: 24px 0;'>
                  <a href='%s' style='background: #023773; color: white; padding: 12px 28px; border-radius: 8px; text-decoration: none; font-weight: 600;'>
                    Accéder à l'application
                  </a>
                </div>
                <p style='color: #dc2626; font-size: 13px;'>⚠️ Merci de changer votre mot de passe dès votre première connexion.</p>
              </div>
            </div>
            """.formatted(role, username, password, appUrl);
    }

    private String buildResetHtml(String resetLink) {
        return """
            <div style='font-family: Arial, sans-serif; max-width: 480px; margin: 0 auto;'>
              <div style='background: linear-gradient(135deg, #023773, #1e4f9c); padding: 32px; border-radius: 12px 12px 0 0;'>
                <h2 style='color: white; margin: 0;'>Réinitialisation du mot de passe</h2>
              </div>
              <div style='background: white; padding: 32px; border: 1px solid #e2e5f0; border-top: none; border-radius: 0 0 12px 12px;'>
                <p>Vous avez demandé la réinitialisation de votre mot de passe.</p>
                <div style='text-align: center; margin: 24px 0;'>
                  <a href='%s' style='background: #023773; color: white; padding: 12px 28px; border-radius: 8px; text-decoration: none; font-weight: 600;'>
                    Réinitialiser mon mot de passe
                  </a>
                </div>
                <p style='color: #6b7280; font-size: 13px;'>Ce lien expire dans 30 minutes. Si vous n'êtes pas à l'origine de cette demande, ignorez cet email.</p>
              </div>
            </div>
            """.formatted(resetLink);
    }
}
