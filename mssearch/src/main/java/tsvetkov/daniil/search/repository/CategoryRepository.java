package tsvetkov.daniil.search.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import tsvetkov.daniil.search.dto.Category;

import java.util.Set;

@Repository
public interface CategoryRepository extends ElasticsearchRepository<Category, Long> {
    Set<Category> findByName(String name);
    Page<Category> findByName(String name, Pageable pageable);
    boolean existsByIndex(Long index);
    void deleteByIndex(Long index);

    Set<Category> findByNameContaining(String name);
}
