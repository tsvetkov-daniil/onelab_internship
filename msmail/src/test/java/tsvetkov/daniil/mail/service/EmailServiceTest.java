package tsvetkov.daniil.mail.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import tsvetkov.daniil.mail.dto.EmailRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmailServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testSendConfirmationEmail() {
        String recipientEmail = "nothing_535@outlook.com";
        String token = "test-token-12345";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        EmailRequest request = new EmailRequest(recipientEmail, token);
        HttpEntity<EmailRequest> entity = new HttpEntity<>(request, headers);

        String response = restTemplate.exchange(
                "/api/email/send-confirmation",
                HttpMethod.POST,
                entity,
                String.class
        ).getBody();

        assertEquals("Email sent to " + recipientEmail, response);

    }
}
