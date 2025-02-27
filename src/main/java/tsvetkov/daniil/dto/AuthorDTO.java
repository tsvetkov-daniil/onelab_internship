package tsvetkov.daniil.dto;

import lombok.*;

@Builder
@Data
public class AuthorDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String pseudonym;
}
