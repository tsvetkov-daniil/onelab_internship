package tsvetkov.daniil.dto;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
@Entity
@Table(name = "review")
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader author;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "score_id")
    private Score score;
}
