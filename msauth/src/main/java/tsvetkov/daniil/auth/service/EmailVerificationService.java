package tsvetkov.daniil.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.auth.entity.EmailVerification;
import tsvetkov.daniil.auth.entity.Reader;
import tsvetkov.daniil.auth.event.KafkaProducerService;
import tsvetkov.daniil.auth.repository.EmailVerificationRepository;
import tsvetkov.daniil.auth.exception.EmailNotFoundException;
import tsvetkov.daniil.exception.InvalidTokenException;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EmailVerificationService {

    private static final int EXPIRE_TIME_IN_HOURS = 6;

    private final EmailVerificationRepository emailVerificationRepository;
    private final ReaderService readerService;
    private final KafkaProducerService kafkaProducerService;

    @Transactional
    public EmailVerification save(EmailVerification emailVerification) {
        return emailVerificationRepository.save(emailVerification);
    }

    public EmailVerification findByEmail(String email) {
        return emailVerificationRepository.findByEmail(email)
                .orElseThrow(EmailNotFoundException::new);
    }

    public EmailVerification findByToken(String token) {
        return emailVerificationRepository.findByToken(token)
                .orElseThrow(InvalidTokenException::new);
    }

    @Transactional
    public void verificate(String token) {
        EmailVerification emailVerification = findByToken(token);

        if (emailVerification.getExpiryDate().isBefore(LocalDateTime.now())) {
            emailVerificationRepository.deleteByToken(token);
            throw new InvalidTokenException();
        }

        Reader reader = readerService.findByEmail(emailVerification.getEmail())
                .orElseThrow(EmailNotFoundException::new);

        reader.setActive(true);
        readerService.save(reader);
        emailVerificationRepository.deleteByToken(token);
    }

    @Transactional
    public void sendConfirmation(String email) {
        emailVerificationRepository.findByEmail(email)
                .ifPresent(ev -> emailVerificationRepository.deleteByToken(ev.getToken()));

        String uuid = UUID.randomUUID().toString();
        EmailVerification emailVerification = EmailVerification.builder()
                .token(uuid)
                .email(email)
                .expiryDate(LocalDateTime.now().plusHours(EXPIRE_TIME_IN_HOURS))
                .build();

        emailVerificationRepository.save(emailVerification);
        kafkaProducerService.sendEmailVerificationToken(emailVerification.getEmail(), emailVerification.getToken());
    }
}
