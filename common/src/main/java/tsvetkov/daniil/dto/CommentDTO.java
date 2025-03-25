package tsvetkov.daniil.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CommentDTO implements Serializable {

    private Long id;

    @JsonProperty("reader_id")
    private Long readerId;

    @JsonProperty("book_id")
    private Long book;

    @JsonProperty("text")
    private String text;

    @JsonProperty("score")
    private ScoreDTO score;
}
