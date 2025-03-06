package tsvetkov.daniil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.dto.Book;
import tsvetkov.daniil.dto.Category;
import tsvetkov.daniil.repository.BookRepository;
import tsvetkov.daniil.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category update(Long id, Category updatedCategory) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));

        existingCategory.setName(updatedCategory.getName());
        existingCategory.setDescription(updatedCategory.getDescription());

        return categoryRepository.save(existingCategory);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    public Set<Category> findSubcategories(Long parentId) {
        return categoryRepository.findByParentCategoryId(parentId);
    }

    // 7️⃣ Добавить книгу в категорию
    @Transactional
    public Category addBookToCategory(Long categoryId, Long bookId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        category.getBooks().add(book);
        return categoryRepository.save(category);
    }

    // 8️⃣ Удалить книгу из категории
    @Transactional
    public Category removeBookFromCategory(Long categoryId, Long bookId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        category.getBooks().remove(book);
        return categoryRepository.save(category);
    }
}
