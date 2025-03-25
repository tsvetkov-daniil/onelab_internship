package tsvetkov.daniil.search.entity;

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
import tsvetkov.daniil.search.util.EnhancedCompletion;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "author")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Text)
    @JsonProperty("first_name")
    private String firstName;

    @Field(type = FieldType.Text)
    @JsonProperty("last_name")
    private String lastName;


    @Field(type = FieldType.Text)
    @JsonProperty("middle_name")
    private String middleName;

    @Field(type = FieldType.Text)
    @JsonIgnore
    private String fullName;

    @CompletionField
    @JsonIgnore
    private transient EnhancedCompletion fullNameSuggest;

    @Field(type = FieldType.Text)
    private String nickname;

    @CompletionField
    @JsonIgnore
    private EnhancedCompletion nicknameSuggest;

}
