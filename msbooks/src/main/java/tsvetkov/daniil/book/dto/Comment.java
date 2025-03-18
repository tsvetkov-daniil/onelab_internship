package tsvetkov.daniil.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    @JsonProperty("id")
    private Long id;

    @Column(name = "reader_id", nullable = false)
    @JsonProperty("reader_id")
    private Long readerId;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    @JsonProperty("book_id")
    private Book book;

    @Column(name = "text", nullable = false)
    @JsonProperty("text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "score_id", nullable = false)
    @JsonProperty("score")
    private Score score;
}
