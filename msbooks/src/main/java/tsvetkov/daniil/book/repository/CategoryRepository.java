package tsvetkov.daniil.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tsvetkov.daniil.book.dto.Category;

import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Set<Category> findByParentCategory_id(Long parentId);
    boolean existsByName(String name);
}
