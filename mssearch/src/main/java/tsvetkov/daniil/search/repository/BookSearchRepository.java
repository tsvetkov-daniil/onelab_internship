package tsvetkov.daniil.search.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import tsvetkov.daniil.search.dto.Book;

import java.util.List;

@Repository
public interface BookSearchRepository extends ElasticsearchRepository<Book, String> {
    List<Book> findByTitleContainingOrDescriptionContaining(String title, String description, Pageable pageable);

    void deleteByIndex(Long index);
}