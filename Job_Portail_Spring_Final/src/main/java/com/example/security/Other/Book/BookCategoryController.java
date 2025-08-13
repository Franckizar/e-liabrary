package com.example.security.Other.Book;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/categories")
@CrossOrigin(origins = "*")
public class BookCategoryController {

    private final BookCategoryService categoryService;

    public BookCategoryController(BookCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Create a new category
    @PostMapping
    public ResponseEntity<BookCategory> createCategory(@RequestBody BookCategory category) {
        BookCategory saved = categoryService.createCategory(category);
        return ResponseEntity.ok(saved);
    }

    // Get all categories
    // @GetMapping
    // public ResponseEntity<List<BookCategory>> getAllCategories() {
    //     List<BookCategory> categories = categoryService.getAllCategories();
    //     return ResponseEntity.ok(categories);
    // }

    // Get single category
    @GetMapping("/{id}")
    public ResponseEntity<BookCategory> getCategory(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update category
    @PutMapping("/{id}")
    public ResponseEntity<BookCategory> updateCategory(
            @PathVariable Long id,
            @RequestBody BookCategory category) {
        try {
            BookCategory updated = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Delete category
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

        // Bulk create categories by name
    @PostMapping("/bulk")
    public ResponseEntity<List<BookCategory>> createCategoriesBulk(@RequestBody List<String> categoryNames) {
        List<BookCategory> saved = categoryService.createCategoriesByName(categoryNames);
        return ResponseEntity.ok(saved);
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<List<BookCategory>> getAllCategories() {
        List<BookCategory> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
