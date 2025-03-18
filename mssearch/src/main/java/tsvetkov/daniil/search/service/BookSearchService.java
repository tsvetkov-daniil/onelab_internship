package tsvetkov.daniil.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tsvetkov.daniil.search.dto.Book;
import tsvetkov.daniil.search.repository.BookSearchRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BookSearchService {
    private final BookSearchRepository bookSearchRepository;
    private final ElasticsearchClient elasticsearchOperations;

    @Autowired
    public BookSearchService(BookSearchRepository bookSearchRepository, ElasticsearchClient elasticsearchOperations) {
        this.bookSearchRepository = bookSearchRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public Book save(Book book) {
        return bookSearchRepository.save(book);
    }

    public Optional<Book> findById(String elasticId) {
        return bookSearchRepository.findById(elasticId);
    }

    public List<Book> searchByTitleOrDescription(String query, int pageNumber, int pageSize) {
        return bookSearchRepository.findByTitleContainingOrDescriptionContaining(query, query, PageRequest.of(pageNumber, pageSize));
    }

    public void deleteById(String elasticId) {
        bookSearchRepository.deleteById(elasticId);
    }

    public List<Book> searchAcrossAllFields(String query, int pageNumber, int pageSize) throws IOException {
        if (StringUtils.isBlank(query)) {
            return List.of();
        }

        SearchResponse<Book> response = elasticsearchOperations.search(s -> s
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
                .map(hit -> hit.source())
                .toList();
    }

    public void deleteByIndex(Long index)
    {
        bookSearchRepository.deleteByIndex(index);
    }

    public void deleteAll() {
        bookSearchRepository.deleteAll();
    }
}