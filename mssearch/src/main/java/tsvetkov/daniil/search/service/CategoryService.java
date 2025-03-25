package tsvetkov.daniil.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tsvetkov.daniil.search.entity.Category;
import tsvetkov.daniil.search.exception.CategoryNotFoundException;
import tsvetkov.daniil.search.repository.CategoryRepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class CategoryService extends AbstractSearchService<Category, String> {

    private static final String CATEGORY_INDEX = "category";

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(ElasticsearchClient elasticsearchClient, CategoryRepository categoryRepository) {
        super(elasticsearchClient, categoryRepository);
        this.categoryRepository = categoryRepository;
    }

    @Override
    protected String getIndexName() {
        return CATEGORY_INDEX;
    }

    @Override
    protected RuntimeException newEntityNotFoundException() {
        return new CategoryNotFoundException();
    }

    @Override
    protected Class<Category> getEntityClass() {
        return Category.class;
    }

    public Set<Category> suggestCategoriesByName(String prefix, Integer size) throws IOException {
        return new HashSet<>(suggestByField(prefix, size, "name"));
    }

    public Set<Category> searchCategoriesByName(String prefix, Integer pageNumber, Integer pageSize) throws IOException {
        return new HashSet<>(searchByField(prefix, pageNumber, pageSize, "name").getContent());
    }

    public Set<Category> findByNameContaining(String name) {
        return categoryRepository.findByNameContaining(name);
    }
}
