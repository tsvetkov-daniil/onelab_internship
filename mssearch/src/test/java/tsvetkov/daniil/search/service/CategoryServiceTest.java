package tsvetkov.daniil.search.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.search.dto.Category;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    private final CategoryService categoryService;

    @Autowired
    CategoryServiceTest(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .index(1L)
                .name("Фантастика")
                .build();
        categoryService.deleteAll();
        categoryService.save(testCategory);
    }

    @Test
    @DisplayName("Сохранение категории")
    void testSave() {
        Category savedCategory = categoryService.save(testCategory);
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("Фантастика");
    }

    @Test
    @DisplayName("Предложение категорий")
    void testSuggestCategories() throws IOException {
        List<String> suggestions = categoryService.suggestCategories("Фан", 5);
        assertThat(suggestions).contains("Фантастика");
    }

    @Test
    @DisplayName("Удаление категории по индексу")
    void testDeleteByIndex() {
        categoryService.deleteByIndex(1L);
        Set<Category> categories = categoryService.findByNameContaining("Фантастика");
        assertThat(categories).isEmpty();
    }
}
