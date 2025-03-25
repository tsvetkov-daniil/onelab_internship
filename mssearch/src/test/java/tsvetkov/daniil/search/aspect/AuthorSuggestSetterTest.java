package tsvetkov.daniil.search.aspect;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import tsvetkov.daniil.search.entity.Author;
import tsvetkov.daniil.search.service.AuthorService;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@EnableAspectJAutoProxy
class AuthorSuggestSetterTest {
    private String firstName;
    private String lastName;
    private Author author;

    private final AuthorService authorService;

    @Autowired
    AuthorSuggestSetterTest(AuthorService authorService) {
        this.authorService = authorService;
    }


    @BeforeEach
    void init()
    {
        firstName = "Иван";
        lastName = "Иванов";
        String middleName = "Иванович";

        author = Author.builder()
                .firstName(firstName)
                .lastName(lastName)
                .middleName(middleName)
                .build();
    }


    @Test
    @DisplayName("Проверка заполнения fullNameSuggest при создании через билдер")
    void testFullNameSuggestOnBuild() {

        authorService.save(author);
        assertThat(author.getFullNameSuggest()).isNotNull();
        assertThat(author.getFullNameSuggest().getInput()).containsExactly("Иван Иванов Иванович");
    }

    @Test
    @DisplayName("Проверка обновления fullNameSuggest при вызове setFirstName")
    void testFullNameSuggestOnSetFirstName() {

        author.setFirstName("Пётр");

        assertThat(author.getFullNameSuggest()).isNotNull();
        assertThat(author.getFullNameSuggest().getInput()).containsExactly("Пётр Иванов Иванович");
    }

    @Test
    @DisplayName("Проверка обновления fullNameSuggest при вызове setLastName")
    void testFullNameSuggestOnSetLastName() {

        author.setLastName("Петров");

        assertThat(author.getFullNameSuggest()).isNotNull();
        assertThat(author.getFullNameSuggest().getInput()).containsExactly("Иван Петров Иванович");
    }

    @Test
    @DisplayName("Проверка обновления fullNameSuggest при вызове setMiddleName")
    void testFullNameSuggestOnSetMiddleName() {
        final String newMiddleName = "Сергеевич";

        author.setMiddleName(newMiddleName);

        assertThat(author.getFullNameSuggest()).isNotNull();

        final String newFullName = firstName + " " + lastName + " " + newMiddleName;
        assertThat(author.getFullNameSuggest().getInput()).containsExactly(newFullName);
    }

    @Test
    @DisplayName("Проверка заполнения fullNameSuggest с null значениями")
    void testFullNameSuggestWithNullValues() {

        final Author author = Author.builder()
                .firstName(firstName)
                .build();

        assertThat(author.getFullNameSuggest()).isNotNull();
        assertThat(author.getFullNameSuggest().getInput()).containsExactly(firstName);
    }
}