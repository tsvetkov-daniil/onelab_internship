package tsvetkov.daniil.mail.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "send_confirmation_email", groupId = "book-group")
    public void consumeMessage(String message) {
        logger.info("Received message: {}", message);

    }
}
