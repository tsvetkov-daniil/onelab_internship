package tsvetkov.daniil.search.repository;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import tsvetkov.daniil.search.dto.Author;

import java.util.List;

public interface AuthorSearchRepository extends ElasticsearchRepository<Author, String> {
    List<Author> findByNicknameContaining(String nickname);

    void deleteByIndex(Long index);

    void deleteAll();
}
