package tsvetkov.daniil.search.service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.search.dto.BookSearchRequest;
import tsvetkov.daniil.search.entity.Book;
import tsvetkov.daniil.search.repository.BookSearchRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class BookSearchService {
    private final BookSearchRepository bookSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticsearchClient elasticsearchClient;


    @Transactional
    public Book save(Book book) {
        return bookSearchRepository.save(book);
    }

    @Transactional(readOnly = true)
    public Optional<Book> findById(Long elasticId) {
        return bookSearchRepository.findById(elasticId);
    }

    @Transactional(readOnly = true)
    public List<Book> searchByTitleOrDescription(String query, int pageNumber, int pageSize) {
        return bookSearchRepository.findByTitleContainingOrDescriptionContaining(query, query, PageRequest.of(pageNumber, pageSize));
    }

    @Transactional
    public void deleteById(Long bookId) {
        bookSearchRepository.deleteById(bookId);
    }

    @Transactional(readOnly = true)
    public Set<Book> searchAcrossAllFields(String query, int pageNumber, int pageSize) throws IOException {
        if (StringUtils.isBlank(query)) {
            return Set.of();
        }

        SearchResponse<Book> response = elasticsearchClient.search(s -> s
                        .index("books")
                        .query(q -> q
                                .multiMatch(m -> m
                                        .query(query)
                                        .fields(List.of("title^2", "description", "authors.firstName", "authors.lastName", "authors.middleName", "authors.nickname", "categories.name"))
                                        .fuzziness("AUTO")
                                        .prefixLength(1)
                                        .maxExpansions(10)
                                )
                        )
                        .from(pageNumber * pageSize)
                        .size(pageSize),
                Book.class);

        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<Book> searchBooks(BookSearchRequest request) {
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        if (request.query() != null && !request.query().isEmpty()) {
            boolQueryBuilder.must(
                    QueryBuilders.match()
                            .field("title")
                            .query(request.query())
                            .fuzziness("AUTO")
                            .build()._toQuery()
            );
        }
        if (request.minPrice() != null || request.maxPrice() != null) {
            boolQueryBuilder.filter(QueryBuilders.range(
                    t -> t.number(j -> j
                            .field("price")
                            .gte(request.minPrice() != null ? request.minPrice() : null)
                            .lte(request.maxPrice() != null ? request.maxPrice() : null)
                    ))
            );
        }

        if (request.categoryId() != null) {
            boolQueryBuilder.filter(
                    QueryBuilders.term()
                            .field("category")
                            .field("id")
                            .value(request.categoryId())
                            .build()._toQuery()
            );
        }

        if (request.authorId() != null) {
            boolQueryBuilder.filter(
                    QueryBuilders.term()
                            .field("author")
                            .field("id")
                            .value(request.authorId())
                            .build()._toQuery()
            );
        }

        NativeQuery query = NativeQuery.builder()
                .withQuery(boolQueryBuilder.build()._toQuery())
                .withPageable(PageRequest.of(request.page(), request.size()))
                .build();

        SearchHits<Book> searchHits = elasticsearchOperations.search(
                query,
                Book.class,
                IndexCoordinates.of("book")
        );

        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void deleteAll() {
        bookSearchRepository.deleteAll();
    }
}