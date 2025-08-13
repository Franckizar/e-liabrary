package com.example.security.Other.Book;

// Add these methods to your BookRepository interface

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Existing method
    Optional<Book> findByIsbn(String isbn);

    // Enhanced query to fetch book with all related data
    @Query("SELECT DISTINCT b FROM Book b " +
           "LEFT JOIN FETCH b.files f " +
           "LEFT JOIN FETCH b.authors a " +
           "LEFT JOIN FETCH a.user au " +
           "LEFT JOIN FETCH b.categories c " +
           "LEFT JOIN FETCH b.publisher p " +
           "LEFT JOIN FETCH p.user pu " +
           "WHERE b.id = :id")
    Optional<Book> findByIdWithFilesAuthorsCategories(@Param("id") Long id);

    // Query to fetch all books with related data
    @Query("SELECT DISTINCT b FROM Book b " +
           "LEFT JOIN FETCH b.files f " +
           "LEFT JOIN FETCH b.authors a " +
           "LEFT JOIN FETCH a.user " +
           "LEFT JOIN FETCH b.categories c " +
           "LEFT JOIN FETCH b.publisher p " +
           "LEFT JOIN FETCH p.user")
    List<Book> findAllWithFilesAuthorsCategories();

    // Find books by author
    @Query("SELECT DISTINCT b FROM Book b " +
           "JOIN b.authors a " +
           "WHERE a.id = :authorId")
    List<Book> findByAuthorId(@Param("authorId") Long authorId);

    // Find books by publisher
    @Query("SELECT b FROM Book b WHERE b.publisher.id = :publisherId")
    List<Book> findByPublisherId(@Param("publisherId") Long publisherId);

    // Find books by category
    @Query("SELECT DISTINCT b FROM Book b " +
           "JOIN b.categories c " +
           "WHERE c.id = :categoryId")
    List<Book> findByCategoryId(@Param("categoryId") Long categoryId);

    // Search books by title or description
    @Query("SELECT DISTINCT b FROM Book b " +
           "LEFT JOIN FETCH b.files f " +
           "LEFT JOIN FETCH b.authors a " +
           "LEFT JOIN FETCH a.user " +
           "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(b.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Book> searchBooks(@Param("searchTerm") String searchTerm);


    // Search by title ignoring case
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Search by format
    List<Book> findByFormat(String format);

    // Optional: Generic search
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Book> searchBooks(String keyword, Pageable pageable);
    
}

