package tsvetkov.daniil.search.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@Document(indexName = "review")
public class Review {
    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Long)
    @JsonProperty("author_id")
    private Long authorId;

    @Field(type = FieldType.Long)
    @JsonProperty("book_id")
    private Long bookId;

    @Field(type = FieldType.Text)
    private String text;

    @Field(type = FieldType.Nested)
    private Score score;
}