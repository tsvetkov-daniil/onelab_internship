package tsvetkov.daniil.auth.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
@Entity
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    private String token;

    @ManyToOne
    private Reader reader;
    @Column(name = "expire_date")
    private Instant expiryDate;
}
