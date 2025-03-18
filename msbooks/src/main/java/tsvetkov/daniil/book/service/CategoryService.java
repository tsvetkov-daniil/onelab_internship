package tsvetkov.daniil.book.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.dto.Category;
import tsvetkov.daniil.book.event.EventProducer;
import tsvetkov.daniil.book.repository.CategoryRepository;

import java.util.List;
import java.util.Set;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventProducer eventProducer;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, EventProducer eventProducer) {
        this.categoryRepository = categoryRepository;
        this.eventProducer = eventProducer;
    }

    public Category findById(Long id) throws IllegalArgumentException {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Категория с таким id не найдена"));
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category save(@Valid Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteById(Long id) throws IllegalArgumentException {
        this.findById(id);
        categoryRepository.deleteById(id);
        eventProducer.removeCatFromSearch(id);
    }


    public Set<Category> findSubcategories(Long id) throws IllegalArgumentException {
        this.findById(id);
        return categoryRepository.findByParentCategory_id(id);
    }

    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
}
