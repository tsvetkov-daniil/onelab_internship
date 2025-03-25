package tsvetkov.daniil.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDTO implements Serializable {

    private Long id;

    @NotNull(message = "Идентификатор автора не может быть пустым")
    @JsonProperty("author_id")
    private Long authorId;

    @NotNull(message = "Идентификатор книги не может быть пустым")
    @JsonProperty("book_id")
    private Long bookId;

    @Size(min = 10, max = 1000, message = "Текст отзыва должен содержать от 10 до 1000 символов")
    @JsonProperty("review_text")
    private String text;

    @Valid
    @NotNull(message = "Оценка не может быть пустой")
    @JsonProperty("review_score")
    private ScoreDTO score;

}
