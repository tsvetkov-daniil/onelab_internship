package tsvetkov.daniil.search.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import tsvetkov.daniil.search.util.AuthorSetDeserializer;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "books")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book implements Serializable {

    @Id
    @JsonIgnore
    private String id;

    @Field(type = FieldType.Long)
    @JsonProperty("id")
    private Long index;

    @Field(type = FieldType.Text)
    @JsonProperty("title")
    private String title;

    @Field(type = FieldType.Text)
    @JsonProperty("description")
    private String description;

    @Field(type = FieldType.Nested)
    @JsonProperty("authors")
    @JsonDeserialize(using = AuthorSetDeserializer.class)
    private Set<Author> authors;

    @Field(type = FieldType.Nested)
    @JsonProperty("categories")
    private Set<Category> categories;


    @Field(type = FieldType.Date)
    @JsonProperty("date")
    private Date publishDate;

    @Field(type = FieldType.Float)
    @JsonProperty("price")
    private Float price;

}
