package tsvetkov.daniil.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReaderDTO implements Serializable {

    private Long id;

    @JsonProperty("photo_url")
    private String photoUrl;

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    @JsonProperty("first_name")
    private String firstName;

    @Size(max = 50, message = "Фамилия должна содержать до 50 символов")
    @JsonProperty("last_name")
    private String lastName;

    @Size(max = 50, message = "Отчество не может быть длиннее 50 символов")
    @JsonProperty("family_name")
    private String familyName;

    @Size(min = 4, max = 15, message = "Никнейм должна содержать от 4 до 15 символов")
    private String username;

    @Email(message = "Некорректный формат email")
    @NotBlank(message = "Email обязателен")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    @JsonIgnoreProperties
    private String password;

    @Size(max = 255, message = "О себе не должно быть длиннее 255 символов")
    @Size
    @JsonProperty("about_me")
    private String aboutMe;

    // TODO SET<>
    @JsonIgnore
    private RoleDTO role;
}
