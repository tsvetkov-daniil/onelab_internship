package tsvetkov.daniil.auth.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginRequest {
    private final String username;
    @NotBlank
    @Size(min = 8, max = 16, message = "Пароль должен быть от 8 до 16 символов")
    private final String password;
}
