package tsvetkov.daniil.mail.service;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender mailSender;


    public void sendConfirmationEmail(String to, String token) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Подтверждение email");
        helper.setText("Для подтверждения перейдите по ссылке: " +
                "http://localhost:8080/api/auth/confirm?token=" + token);
        helper.setFrom("no-reply@gmail.com");

        mailSender.send(message);
    }
}
