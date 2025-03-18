package tsvetkov.daniil.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.search.dto.Category;
import tsvetkov.daniil.search.repository.CategoryRepository;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final ElasticsearchClient elasticsearchClient;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(ElasticsearchClient elasticsearchClient, CategoryRepository categoryRepository) {
        this.elasticsearchClient = elasticsearchClient;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public List<String> suggestCategories(String prefix, Integer size) throws IOException {
        SearchResponse<Category> response = elasticsearchClient.search(s -> s
                        .index("category")
                        .suggest(sug -> sug
                                .suggesters("name-suggest", s1 -> s1
                                        .completion(c -> c
                                                .field("name")
                                                .skipDuplicates(true)
                                                .size(size)
                                        )
                                        .prefix(prefix)
                                )
                        ),
                Category.class);

        return response.suggest().values().stream()
                .flatMap(List::stream)
                .flatMap(suggestion -> suggestion.completion().options().stream())
                .map(CompletionSuggestOption::text)
                .distinct()
                .collect(Collectors.toList());
    }

    public void deleteByIndex(Long index) {
        categoryRepository.deleteByIndex(index);
    }

    public void deleteAll() {
    categoryRepository.deleteAll();
    }

    public Set<Category> findByNameContaining(String name) {
        return categoryRepository.findByNameContaining(name);
    }
}
