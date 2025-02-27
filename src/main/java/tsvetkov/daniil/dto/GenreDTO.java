package tsvetkov.daniil.dto;

import lombok.*;

@Builder
@Data
public class GenreDTO {
    private Integer id;
    private String name;
    private String description;
}
