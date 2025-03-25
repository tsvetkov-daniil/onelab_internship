package tsvetkov.daniil.search.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsvetkov.daniil.search.entity.Category;
import tsvetkov.daniil.search.service.CategoryService;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/search/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> saveCategory(@RequestBody @Valid Category category) {
        return ResponseEntity.ok(categoryService.save(category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @GetMapping("/suggest/name")
    public ResponseEntity<Set<Category>> suggestCategoriesByName(
            @RequestParam String prefix,
            @RequestParam(defaultValue = "5") Integer size) throws IOException {
        return ResponseEntity.ok(categoryService.suggestCategoriesByName(prefix, size));
    }

    @GetMapping("/search/name")
    public ResponseEntity<Set<Category>> searchCategoriesByName(
            @RequestParam String prefix,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) throws IOException {
        return ResponseEntity.ok(categoryService.searchCategoriesByName(prefix, page, pageSize));
    }

    @GetMapping("/search/contains")
    public ResponseEntity<Set<Category>> findByNameContaining(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.findByNameContaining(name));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
