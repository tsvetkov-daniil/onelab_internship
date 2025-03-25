package tsvetkov.daniil.book.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.entity.Category;
import tsvetkov.daniil.book.repository.CategoryRepository;

import java.util.List;
import java.util.Set;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EventProducer eventProducer;

    private Category createValidCategory() {
        return Category.builder()
                .name("Тестовая категория")
                .description("Описание тестовой категории")
                .build();
    }

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("Успешное сохранение новой категории")
    void testSave() {
        Category category = createValidCategory();
        Category savedCategory = categoryService.save(category);

        Assertions.assertThat(savedCategory.getId()).isNotNull();
        Assertions.assertThat(savedCategory.getName()).isEqualTo("Тестовая категория");
        Assertions.assertThat(savedCategory.getDescription()).isEqualTo("Описание тестовой категории");
    }

    @Test
    @DisplayName("Поиск категории по ID")
    void testFindById() {
        Category category = createValidCategory();
        Category savedCategory = categoryService.save(category);

        Category foundCategory = categoryService.findById(savedCategory.getId());

        Assertions.assertThat(foundCategory).isEqualTo(savedCategory);
    }

    @Test
    @DisplayName("Ошибка при поиске несуществующей категории по ID")
    void testFindByIdNotFound() {
        Assertions.assertThatThrownBy(() -> categoryService.findById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Категория с таким id не найдена");
    }

    @Test
    @DisplayName("Получение всех категорий")
    void testFindAll() {
        categoryService.save(createValidCategory());
        categoryService.save(Category.builder()
                .name("Другая категория")
                .description("Другое описание")
                .build());

        List<Category> categories = categoryService.findAll();

        Assertions.assertThat(categories).hasSize(2);
        Assertions.assertThat(categories).extracting("name").contains("Тестовая категория", "Другая категория");
    }

    @Test
    @DisplayName("Удаление категории по ID")
    void testDeleteById() {
        Category category = createValidCategory();
        Category savedCategory = categoryService.save(category);

        categoryService.deleteById(savedCategory.getId());

        Assertions.assertThatThrownBy(() -> categoryService.findById(savedCategory.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Категория с таким id не найдена");
    }

    @Test
    @DisplayName("Ошибка при удалении несуществующей категории")
    public void testDeleteByIdNotFound() {
        Assertions.assertThatThrownBy(() -> categoryService.deleteById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Категория с таким id не найдена");
    }

    @Test
    @DisplayName("Поиск подкатегорий")
    void testFindSubcategories() {
        Category parentCategory = categoryService.save(createValidCategory());
        Category subcategory = Category.builder()
                .name("Подкатегория")
                .description("Описание подкатегории")
                .parentCategory(parentCategory)
                .build();
        categoryService.save(subcategory);

        Set<Category> subcategories = categoryService.findSubcategories(parentCategory.getId());

        Assertions.assertThat(subcategories).hasSize(1);
        Assertions.assertThat(subcategories).extracting("name").contains("Подкатегория");
    }

    @Test
    @DisplayName("Ошибка при поиске подкатегорий для несуществующей категории")
    void testFindSubcategoriesNotFound() {
        Assertions.assertThatThrownBy(() -> categoryService.findSubcategories(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Категория с таким id не найдена");
    }

    @Test
    @DisplayName("Проверка существования категории по имени")
    void testExistsByName() {
        Category category = createValidCategory();
        categoryService.save(category);

        boolean exists = categoryService.existsByName("Тестовая категория");
        boolean notExists = categoryService.existsByName("Несуществующая категория");

        Assertions.assertThat(exists).isTrue();
        Assertions.assertThat(notExists).isFalse();
    }
}