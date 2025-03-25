package tsvetkov.daniil.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.Hit;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@MappedSuperclass
public abstract class AbstractSearchService<E, ID> {

    protected final ElasticsearchClient elasticsearchClient;
    protected final ElasticsearchRepository<E, ID> repository;

    protected <R extends ElasticsearchRepository<E, ID>> AbstractSearchService(ElasticsearchClient elasticsearchClient, R repository) {
        this.elasticsearchClient = elasticsearchClient;
        this.repository = repository;
    }

    protected abstract String getIndexName();

    @Transactional
    public E save(@Valid E entity) {
        return repository.save(entity);
    }

    public E findById(ID id) {
        return repository.findById(id)
                .orElseThrow(this::newEntityNotFoundException);
    }

    @Transactional
    public void deleteById(ID id) {
        findById(id);
        repository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    public List<E> suggestByField(String prefix, Integer size, String field) throws IOException {
        SearchResponse<E> response = elasticsearchClient.search(s -> s
                        .index(getIndexName())
                        .suggest(sug -> sug
                                .suggesters(field + "-suggest", s1 -> s1
                                        .completion(c -> c
                                                .field(field + "Suggest")
                                                .skipDuplicates(true)
                                                .size(size))
                                        .prefix(prefix))),
                getEntityClass());

        return response.suggest().values().stream()
                .flatMap(List::stream)
                .flatMap(suggestion -> suggestion.completion().options().stream())
                .map(CompletionSuggestOption::source)
                .distinct()
                .toList();
    }

    public Page<E> searchByField(String prefix, Integer pageNumber, Integer pageSize, String field) throws IOException {
        SearchResponse<E> response = elasticsearchClient.search(s -> s
                        .index(getIndexName())
                        .query(q -> q
                                .matchPhrasePrefix(m -> m
                                        .field(field)
                                        .query(prefix)))
                        .from(pageNumber * pageSize)
                        .size(pageSize),
                getEntityClass());

        List<E> entities = response.hits().hits().stream()
                .map(Hit::source)
                .toList();

        long totalHits = response.hits().total() != null ? response.hits().total().value() : 0;

        return new PageImpl<>(entities, PageRequest.of(pageNumber, pageSize), totalHits);
    }

    protected abstract RuntimeException newEntityNotFoundException();

    protected abstract Class<E> getEntityClass();
}