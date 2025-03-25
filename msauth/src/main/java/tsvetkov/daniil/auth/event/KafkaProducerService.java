package tsvetkov.daniil.auth.event;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tsvetkov.daniil.dto.VerifyEmailMessage;

@AllArgsConstructor
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendEmailVerificationToken(String email, String token) {

        kafkaTemplate.send("send_confirmation_email", String.valueOf(new VerifyEmailMessage(email,token)));
    }
}
