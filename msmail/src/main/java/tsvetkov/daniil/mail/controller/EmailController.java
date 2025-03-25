package tsvetkov.daniil.mail.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tsvetkov.daniil.mail.dto.EmailRequest;
import tsvetkov.daniil.mail.service.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-confirmation")
    public String sendConfirmation(@RequestBody EmailRequest request) throws Exception {
        emailService.sendConfirmationEmail(request.email(), request.token());
        return "Email sent to " + request.email();
    }
}
