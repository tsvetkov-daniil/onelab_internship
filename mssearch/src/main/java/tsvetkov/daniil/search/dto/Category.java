package tsvetkov.daniil.search.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "category")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category implements Serializable {

    @Id
    @JsonIgnore
    private String id;

    @Field(type = FieldType.Long)
    @JsonProperty("id")
    private Long index;

    @CompletionField
    @JsonProperty("name")
    private String name;

}
