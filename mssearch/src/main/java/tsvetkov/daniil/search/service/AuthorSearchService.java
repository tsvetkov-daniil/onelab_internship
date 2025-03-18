package tsvetkov.daniil.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.search.dto.Author;
import tsvetkov.daniil.search.repository.AuthorSearchRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorSearchService {
    private final AuthorSearchRepository authorSearchRepository;
    private final ElasticsearchClient elasticsearchClient;

    @Autowired
    public AuthorSearchService(AuthorSearchRepository authorSearchRepository, ElasticsearchClient elasticsearchClient) {
        this.authorSearchRepository = authorSearchRepository;
        this.elasticsearchClient = elasticsearchClient;
    }

    public Author save(Author author) {
        return authorSearchRepository.save(author);
    }

    public Optional<Author> findById(String elasticId) {
        return authorSearchRepository.findById(elasticId);
    }

    public List<Author> findByNickname(String nickname) {
        return authorSearchRepository.findByNicknameContaining(nickname);
    }

    public List<String> suggestAuthorNames(String prefix, Integer size) throws IOException {
        SearchResponse<Author> response = elasticsearchClient.search(s -> s
                        .index("author")
                        .suggest(sug -> sug
                                .suggesters("firstName-suggest", s1 -> s1
                                        .completion(c -> c
                                                .field("firstName")
                                                .skipDuplicates(true)
                                                .size(10)
                                        )
                                        .prefix(prefix)
                                )
                                .suggesters("lastName-suggest", s1 -> s1
                                        .completion(c -> c
                                                .field("lastName")
                                                .skipDuplicates(true)
                                                .size(size)
                                        )
                                        .prefix(prefix)
                                )
                                .suggesters("middleName-suggest", s1 -> s1
                                        .completion(c -> c
                                                .field("middleName")
                                                .skipDuplicates(true)
                                                .size(size)
                                        )
                                        .prefix(prefix)
                                )
                                .suggesters("nickname-suggest", s1 -> s1
                                        .completion(c -> c
                                                .field("nickname")
                                                .skipDuplicates(true)
                                                .size(size)
                                        )
                                        .prefix(prefix)
                                )
                        ),
                Author.class);

        return response.suggest().values().stream()
                .flatMap(List::stream)
                .flatMap(suggestion -> suggestion.completion().options().stream())
                .map(CompletionSuggestOption::text)
                .distinct()
                .toList();
    }


    public void deleteById(String elasticId) {
        authorSearchRepository.deleteById(elasticId);
    }

    public void deleteByIndex(Long elasticId) {
        authorSearchRepository.deleteByIndex(elasticId);
    }

    @Transactional
    public void deleteAll() {
        authorSearchRepository.deleteAll();
    }
}