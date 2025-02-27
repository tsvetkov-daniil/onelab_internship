package tsvetkov.daniil.dto;

import lombok.*;

import java.util.Set;

@Builder
@Data
public class BookDTO {
    private Integer id;
    private String description;
    private String title;
    private Set<AuthorDTO> authors;
    private Set<GenreDTO> genres;
}
