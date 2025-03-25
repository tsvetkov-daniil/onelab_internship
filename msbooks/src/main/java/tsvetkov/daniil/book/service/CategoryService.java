package tsvetkov.daniil.book.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tsvetkov.daniil.book.entity.Category;
import tsvetkov.daniil.book.exception.CategoryNotFoundException;
import tsvetkov.daniil.book.repository.CategoryRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Validated
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventProducer eventProducer;

    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Set<Category> findAll(Integer pageNumber, Integer pageSize) {
        return new HashSet<>(categoryRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent());
    }

    @Transactional
    public Category save(@Valid Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteById(Long id) {
        findById(id);
        categoryRepository.deleteById(id);
        eventProducer.removeCatFromSearch(id);
    }

    @Transactional(readOnly = true)
    public Set<Category> findSubcategories(Long id) {
        findById(id);
        return categoryRepository.findByParentCategoryId(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
}
