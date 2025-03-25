package tsvetkov.daniil.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import tsvetkov.daniil.search.entity.Review;

import java.util.Set;

@Repository
public interface ReviewSearchRepository extends ElasticsearchRepository<Review, String> {
    Set<Review> findByBookId(Long bookId);
}
