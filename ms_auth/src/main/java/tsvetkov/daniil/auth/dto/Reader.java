package tsvetkov.daniil.auth.dto;

import jakarta.persistence.*;
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
@Builder
@Data
@Entity
@Table(name = "reader")
public class Reader implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reader_id")
    private Long id;

    @Column(name = "photo_url")
    private String photoUrl;

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Фамилия не должна быть пустой")
    @Size(max = 50, message = "Фамилия должна содержать до 50 символов")
    @Column(name = "last_name")
    private String lastName;

    @Size(max = 50, message = "Отчество не может быть длиннее 50 символов")
    @Column(name = "family_name")
    private String familyName;

    @Size(min = 4, max = 15, message = "Никнейм должна содержать от 4 до 15 символов")
    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Email(message = "Некорректный формат email")
    @NotBlank(message = "Email обязателен")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Size(max = 255, message = "О себе не должно быть длиннее 255 символов")
    @Column(name = "about_me")
    private String aboutMe;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
