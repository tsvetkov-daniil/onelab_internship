package tsvetkov.daniil.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import tsvetkov.daniil.dto.ReaderDTO;

@Builder
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {
    private final String accessToken;
    private final String refreshToken;
    private final ReaderDTO reader;
}
