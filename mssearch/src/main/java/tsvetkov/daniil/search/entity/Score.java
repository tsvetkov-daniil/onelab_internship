package tsvetkov.daniil.search.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@Document(indexName = "score")
public class Score{

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Byte)
    private Byte value;
}
