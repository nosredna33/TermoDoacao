package br.gov.saude.termosentrega.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    private final JavaMailSender mailSender;
    
    @Value("${app.mail.from}")
    private String mailFrom;
    
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void enviarEmailRecuperacaoSenha(String destinatario, String resetUrl) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(destinatario);
            message.setSubject("Recuperação de Senha - Sistema de Termos de Entrega");
            message.setText(
                "Você solicitou a recuperação de senha.\n\n" +
                "Clique no link abaixo para redefinir sua senha:\n" +
                resetUrl + "\n\n" +
                "Este link é válido por 24 horas.\n\n" +
                "Se você não solicitou esta recuperação, ignore este e-mail.\n\n" +
                "Atenciosamente,\n" +
                "Sistema de Termos de Entrega"
            );
            
            mailSender.send(message);
            logger.info("E-mail de recuperação de senha enviado para: {}", destinatario);
        } catch (Exception e) {
            logger.error("Erro ao enviar e-mail de recuperação de senha", e);
        }
    }
}
