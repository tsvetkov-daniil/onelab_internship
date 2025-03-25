package tsvetkov.daniil.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import tsvetkov.daniil.search.entity.Author;
import tsvetkov.daniil.search.exception.AuthorNotFoundException;
import tsvetkov.daniil.search.repository.AuthorSearchRepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthorService extends AbstractSearchService<Author, String> {

    private static final String AUTHOR_INDEX = "author";
    private final AuthorSearchRepository authorSearchRepository;

    @Autowired
    public AuthorService(ElasticsearchClient elasticsearchClient, AuthorSearchRepository authorSearchRepository) {
        super(elasticsearchClient,  authorSearchRepository);
        this.authorSearchRepository = authorSearchRepository;
    }

    @Override
    protected String getIndexName() {
        return AUTHOR_INDEX;
    }

    @Override
    protected RuntimeException newEntityNotFoundException() {
        return new AuthorNotFoundException();
    }

    @Override
    protected Class<Author> getEntityClass() {
        return Author.class;
    }

    public Set<Author> suggestAuthorName(String prefix, Integer size) throws IOException {
        return new HashSet<>(suggestByField(prefix, size, "fullName"));
    }

    public Set<Author> suggestAuthorNickname(String prefix, Integer size) throws IOException {
        return new HashSet<>(suggestByField(prefix, size, "nickname"));
    }

    public Page<Author> searchAuthorName(String prefix, Integer pageNumber, Integer pageSize) throws IOException {
        return searchByField(prefix, pageNumber, pageSize, "fullName");
    }

    public Page<Author> searchAuthorNickname(String prefix, Integer pageNumber, Integer pageSize) throws IOException {
        return searchByField(prefix, pageNumber, pageSize, "nickname");
    }
}