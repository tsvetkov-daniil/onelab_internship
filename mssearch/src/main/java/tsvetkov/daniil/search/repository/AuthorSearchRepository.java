package tsvetkov.daniil.search.repository;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import tsvetkov.daniil.search.entity.Author;

@Repository
public interface AuthorSearchRepository extends ElasticsearchRepository<Author, String> {
    void deleteById(Long id);
    void deleteAll();
}
