package tsvetkov.daniil.book.dto;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    @JsonProperty("id")
    private Long id;

    @Column(name = "cover_url")
    @JsonProperty("cover_url")
    @URL(message = "Неверный формат url")
    private String coverUrl;

    @Column(name = "description", nullable = false)
    @JsonProperty("description")
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Описание должно быть максимум 1000 символов")
    private String description;

    @Temporal(TemporalType.DATE)
    @Column(name = "release_date", nullable = false)
    //@JsonProperty("publish_date")
    private Date publishDate;

    @Column(name = "title", nullable = false)
    @JsonProperty("title")
    @NotBlank(message = "Должно быть название")
    @Size(max = 255, message = "Название должно быть от 1 до 255 символов")
    private String title;

    @ManyToOne
    @JoinColumn(name = "language_id")
    @JsonProperty("language")
    @NotNull(message = "Укажите язык")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "status_id")
    @JsonProperty("status")
    @NotNull(message = "Укажите статус статус")
    private BookStatus status;

    @Column(name = "price", nullable = false)
    @JsonProperty("price")
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Цена не может быть отрицательной")
    private Float price;

    @ElementCollection
    @CollectionTable(name = "author_book", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "user_id", nullable = false)
    @JsonProperty("authors")
    @NotEmpty(message = "Книга должна иметь хотя бы одного автора")
    private Set<Long> authors;

    @ElementCollection
    @CollectionTable(name = "favourite", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "user_id")
    @JsonIgnore
    private Set<Long> likes;

    @ManyToMany
    @JoinTable(
            name = "category_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonProperty("categories")
    @NotEmpty(message = "Книга должна быть хотя бы в одной категории")
    private Set<Category> categories;
}

