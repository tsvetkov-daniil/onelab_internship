package tsvetkov.daniil.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import tsvetkov.daniil.search.entity.Category;

import java.util.Set;

@Repository
public interface CategoryRepository extends ElasticsearchRepository<Category, String> {
    boolean existsById(Long index);
    void deleteById(Long index);

    Set<Category> findByNameContaining(String name);
}
