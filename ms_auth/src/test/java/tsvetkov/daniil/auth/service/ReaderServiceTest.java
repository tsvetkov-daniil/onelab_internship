package tsvetkov.daniil.auth.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.auth.dto.Reader;
import tsvetkov.daniil.auth.dto.Role;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;


@SpringBootTest
@Transactional
@Rollback
class ReaderServiceTest {

    private final ReaderService readerService;
    private final RoleService roleService;

    @Autowired
    ReaderServiceTest(ReaderService readerService, RoleService roleService) {
        this.readerService = readerService;
        this.roleService = roleService;
    }


    @Test
    @DisplayName("Сохранение нового пользователя")
    void testSaveNewReader()  {
        final String email = "test@example.com";
        final Reader reader = createTestReader();
        reader.setEmail(email);

        assertThatCode(()-> readerService.save(reader)).doesNotThrowAnyException();

        assertThat(reader.getId()).isNotNull();
        assertThat(reader.getEmail()).isEqualTo(email);
        assertThat(reader.getNickname()).isNotNull();
    }

    @Test
    @DisplayName("Поиск пользователя по id")
    void testFindById() {
        final Reader reader = createTestReader();

        assertThatCode(() -> readerService.save(reader)).doesNotThrowAnyException();
        final Optional<Reader> foundReader = readerService.findById(reader.getId());

        assertThat(foundReader).isPresent()
                .hasValueSatisfying(r ->
                        assertThat(r.getId()).isEqualTo(reader.getId())
                );

    }

    @Test
    @DisplayName("Поиск пользователя по email")
    void testFindByEmail() {
        final String email = "findme@example.com";
        final Reader reader = createTestReader();
        reader.setEmail(email);

        assertThatCode(() -> readerService.save(reader)).doesNotThrowAnyException();

        final Optional<Reader> foundReader = readerService.findByEmail(email);

        assertThat(foundReader).isPresent()
                .hasValueSatisfying(r ->
                {
                    assertThat(r.getEmail()).isEqualTo(email);
                });
    }

    @Test
    @DisplayName("Удаление пользователя по id")
    void testDeleteById() throws Exception {
        final Reader reader = createTestReader();
        readerService.save(reader);

        readerService.deleteById(reader.getId());

        final Optional<Reader> foundReader = readerService.findById(reader.getId());
        assertThat(foundReader).isNotPresent();
    }

    @Test
    @DisplayName("Получить всех пользователей")
    void testFindAll() {
        final Reader reader1 = createTestReader();
        reader1.setEmail("user1@example.com");

        final Reader reader2 = createTestReader();
        reader2.setEmail("user2@example.com");
        reader2.setNickname("user2");

        assertThatCode(() -> readerService.save(reader1)).doesNotThrowAnyException();
        assertThatCode(() -> readerService.save(reader2)).doesNotThrowAnyException();

        final Set<Reader> readers = readerService.findAll(0, 10);

        assertThat(readers).hasSize(2);
    }

    private Reader createTestReader() {
        final Reader reader = Reader.builder()
                .firstName("Иван")
                .lastName("Иванов")
                .familyName("Иванович")
                .nickname("ivan123")
                .email("ivan@example.com")
                .aboutMe("Люблю читать книги.")
                .photoUrl("https://example.com/photo.jpg")
                .build();

        final Role defaultRole = roleService.getDefaultRole()
                .orElseThrow(() -> new RuntimeException("Дефолтная роль не найдена"));
        reader.setRole(defaultRole);

        return reader;
    }

}
