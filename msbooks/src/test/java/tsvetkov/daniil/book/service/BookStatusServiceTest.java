package tsvetkov.daniil.book.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.dto.BookStatus;
import tsvetkov.daniil.book.repository.BookStatusRepository;

import java.util.Optional;
import java.util.Set;

@SpringBootTest
@Transactional
class BookStatusServiceTest {

    @Autowired
    private BookStatusService bookStatusService;

    @Autowired
    private BookStatusRepository bookStatusRepository;

    private BookStatus createValidBookStatus() {
        return BookStatus.builder()
                .title("Тестовый статус")
                .description("Описание тестового статуса")
                .build();
    }

    @BeforeEach
    void setUp() {
        bookStatusRepository.deleteAll();
    }

    @Test
    @DisplayName("Успешное сохранение нового статуса книги")
    void testSave() {
        BookStatus bookStatus = createValidBookStatus();
        BookStatus savedStatus = bookStatusService.save(bookStatus);

        Assertions.assertThat(savedStatus.getId()).isNotNull();
        Assertions.assertThat(savedStatus.getTitle()).isEqualTo("Тестовый статус");
        Assertions.assertThat(savedStatus.getDescription()).isEqualTo("Описание тестового статуса");
    }

    @Test
    @DisplayName("Поиск статуса по ID")
    void testFindById() {
        BookStatus bookStatus = createValidBookStatus();
        BookStatus savedStatus = bookStatusService.save(bookStatus);

        BookStatus foundStatus = bookStatusService.findById(savedStatus.getId());

        Assertions.assertThat(foundStatus).isEqualTo(savedStatus);
    }

    @Test
    @DisplayName("Ошибка при поиске несуществующего статуса по ID")
    void testFindByIdNotFound() {
        Assertions.assertThatThrownBy(() -> bookStatusService.findById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Статус с таким id не найден");
    }

    @Test
    @DisplayName("Получение списка статусов с пагинацией")
    void testFindAll() {
        bookStatusService.save(createValidBookStatus());
        bookStatusService.save(BookStatus.builder()
                .title("Другой статус")
                .description("Другое описание")
                .build());

        Set<BookStatus> statuses = bookStatusService.findAll(0, 1);

        Assertions.assertThat(statuses).hasSize(1);
    }

    @Test
    @DisplayName("Удаление статуса по ID")
    void testDeleteById() {
        BookStatus bookStatus = createValidBookStatus();
        BookStatus savedStatus = bookStatusService.save(bookStatus);

        bookStatusService.deleteById(savedStatus.getId());

        Assertions.assertThatThrownBy(() -> bookStatusService.findById(savedStatus.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Статус с таким id не найден");
    }

    @Test
    @DisplayName("Ошибка при удалении несуществующего статуса")
    void testDeleteByIdNotFound() {
        Assertions.assertThatThrownBy(() -> bookStatusService.deleteById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Статус книги не найден");
    }

    @Test
    @DisplayName("Поиск статуса по названию")
    void testFindByTitle() {
        BookStatus bookStatus = createValidBookStatus();
        bookStatusService.save(bookStatus);

        Optional<BookStatus> foundStatus = bookStatusService.findByTitle("Тестовый статус");

        Assertions.assertThat(foundStatus).isPresent();
        Assertions.assertThat(foundStatus.get().getTitle()).isEqualTo("Тестовый статус");
    }

    @Test
    @DisplayName("Поиск несуществующего статуса по названию")
    void testFindByTitleNotFound() {
        Optional<BookStatus> foundStatus = bookStatusService.findByTitle("Несуществующий статус");

        Assertions.assertThat(foundStatus).isNotPresent();
    }

    @Test
    @DisplayName("Удаление всех статусов")
    void testDeleteAll() {
        bookStatusService.save(createValidBookStatus());
        bookStatusService.save(BookStatus.builder()
                .title("Другой статус")
                .description("Другое описание")
                .build());

        bookStatusService.deleteAll();

        Assertions.assertThat(bookStatusRepository.findAll(PageRequest.of(0, 10)).getContent()).isEmpty();
    }

    @Test
    @DisplayName("Получение дефолтного статуса, если он существует")
    void testGetDefaultStatusExisting() {
        BookStatus defaultStatus = BookStatus.builder()
                .title("Added")
                .description("Существующий дефолтный статус")
                .build();
        bookStatusService.save(defaultStatus);

        BookStatus result = bookStatusService.getDefaultStatus();

        Assertions.assertThat(result.getTitle()).isEqualTo("Added");
        Assertions.assertThat(result.getDescription()).isEqualTo("Существующий дефолтный статус");
    }

    @Test
    @DisplayName("Создание дефолтного статуса, если он не существует")
    void testGetDefaultStatusNotExisting() {
        BookStatus result = bookStatusService.getDefaultStatus();

        Assertions.assertThat(result.getId()).isNotNull();
        Assertions.assertThat(result.getTitle()).isEqualTo("Added");
        Assertions.assertThat(result.getDescription()).isEqualTo("Книга опубликована");
    }
}