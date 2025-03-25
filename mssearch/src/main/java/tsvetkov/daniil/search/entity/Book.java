package tsvetkov.daniil.search.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import tsvetkov.daniil.search.util.AuthorSetDeserializer;
import tsvetkov.daniil.search.util.EnhancedCompletion;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "book")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book{

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    @CompletionField
    @JsonIgnore
    private EnhancedCompletion titleSuggest;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Nested)
    @JsonDeserialize(using = AuthorSetDeserializer.class)
    private Set<Author> authors;

    @Field(type = FieldType.Nested)
    private Set<Category> categories;


    @Field(type = FieldType.Date)
    @JsonProperty("publish_date")
    private Date publishDate;

    @Field(type = FieldType.Float)
    private Float price;

}
