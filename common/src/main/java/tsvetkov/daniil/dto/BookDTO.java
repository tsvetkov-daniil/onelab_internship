package tsvetkov.daniil.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDTO implements Serializable {

    private Long id;

    @JsonProperty("cover_url")
    @URL(message = "Неверный формат url")
    private String coverUrl;

    @JsonProperty("description")
    @Size(min = 200, max = 1000, message = "Описание должно быть от 200 до 1000 символов")
    private String description;

    @Temporal(TemporalType.DATE)
    @JsonProperty("publish_date")
    private Date publishDate;

    @JsonProperty("title")
    @NotBlank(message = "Должно быть название")
    @Size(max = 255, message = "Название должно быть от 1 до 255 символов")
    private String title;

    @NotNull(message = "Укажите язык")
    private LanguageDTO language;

    @JsonProperty("status")
    @NotNull(message = "Укажите статус статус")
    private BookStatusDTO status;

    @JsonProperty("price")
    @Min(value = 0, message = "Цена не может быть отрицательной")
    private Float price;

    @NotEmpty(message = "Книга должна иметь хотя бы одного автора")
    private Set<ReaderDTO> authors;

    @JsonIgnore
    private Set<Long> likes;


    @JsonProperty("categories")
    @NotEmpty(message = "Книга должна быть хотя бы в одной категории")
    private Set<CategoryDTO> categories;
}
