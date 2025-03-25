package tsvetkov.daniil.book.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.entity.Category;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Rollback
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("Поиск родительских категорий")
    void testFindByParentcategoryid() {
        Category parentCategory = categoryRepository.save(Category.builder().name("Parent").build());

        Category child1 = categoryRepository.save(Category.builder().name("Child 1").parentCategory(parentCategory).build());
        Category child2 = categoryRepository.save(Category.builder().name("Child 2").parentCategory(parentCategory).build());

        Set<Category> foundCategories = categoryRepository.findByParentCategory_id(parentCategory.getId());

        assertThat(foundCategories).containsExactlyInAnyOrder(child1, child2);
    }

    @Test
    void testExistsByName() {
        final String existCategory = "Existing Category";
        categoryRepository.save(Category.builder().name(existCategory).build());

        assertThat(categoryRepository.existsByName(existCategory)).isTrue();
        assertThat(categoryRepository.existsByName("Non-existing Category")).isFalse();
    }
}
