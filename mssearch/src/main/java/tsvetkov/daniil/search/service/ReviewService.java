package tsvetkov.daniil.search.service;

import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.search.entity.Review;
import tsvetkov.daniil.search.repository.ReviewSearchRepository;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ReviewService {
    private final ReviewSearchRepository reviewSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;


    @Transactional
    public void save(Review review) {
        reviewSearchRepository.save(review);
    }

    @Transactional(readOnly = true)
    public Set<Review> findByBookId(Long bookId) {
        return reviewSearchRepository.findByBookId(bookId);
    }

    @Transactional(readOnly = true)
    public Set<Review> findByFuzzyText(String searchText) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .match(m -> m
                                .field("text")
                                .query(searchText)
                                .fuzziness("AUTO")
                                .prefixLength(3)))
                .build();

        SearchHits<Review> searchHits = elasticsearchOperations.search(
                query,
                Review.class,
                IndexCoordinates.of("reviews")
        );

        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void deleteById(Long reviewId) {
        reviewSearchRepository.deleteById(reviewId);
    }
}
