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
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    @JsonProperty("id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @JsonProperty("name")
    private String name;

    @Column(name = "description", unique = true)
    @JsonProperty("description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    @JsonProperty("parent_category")
    private Category parentCategory;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "category_book",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    @JsonProperty("books")
    private Set<Book> books;
}
