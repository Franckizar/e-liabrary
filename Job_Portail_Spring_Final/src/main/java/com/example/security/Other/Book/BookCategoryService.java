package com.example.security.Other.Book;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookCategoryService {

    private final BookCategoryRepository categoryRepository;

    public BookCategoryService(BookCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Create a new category
    public BookCategory createCategory(BookCategory category) {
        return categoryRepository.save(category);
    }

    // Get all categories
    // public List<BookCategory> getAllCategories() {
    //     return categoryRepository.findAll();
    // }

    // Get category by ID
    public Optional<BookCategory> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // Update category
    public BookCategory updateCategory(Long id, BookCategory updatedCategory) {
        BookCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        category.setName(updatedCategory.getName());
        return categoryRepository.save(category);
    }

    // Delete category
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }


     // Create multiple categories at once by name
    public List<BookCategory> createCategoriesByName(List<String> names) {
        List<BookCategory> categories = names.stream()
                .map(name -> {
                    BookCategory c = new BookCategory();
                    c.setName(name);
                    return c;
                })
                .collect(Collectors.toList());
        return categoryRepository.saveAll(categories);
    }

    // Get all categories
    public List<BookCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
}
