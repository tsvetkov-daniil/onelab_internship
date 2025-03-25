package tsvetkov.daniil.auth.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@Entity
@Table(name = "email_verification")
public class EmailVerification {
    @Id
    private String token;

    @Column(nullable = false, unique = true)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expire_date",nullable = false)
    @JsonProperty("expire_date")
    private LocalDateTime expiryDate;
}
