package com.example.security.Other.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    Optional<Book> findByIsbn(String isbn);
    
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.id = :authorId")
    List<Book> findByAuthorId(@Param("authorId") Long authorId);
    
    @Query("SELECT b FROM Book b WHERE b.publisher.id = :publisherId")
    List<Book> findByPublisherId(@Param("publisherId") Long publisherId);
    
    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    List<Book> findByCategoryId(@Param("categoryId") Long categoryId);
    
    @Query("SELECT b FROM Book b WHERE b.format = :format")
    List<Book> findByFormat(@Param("format") String format);
    
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword% OR b.description LIKE %:keyword%")
    Page<Book> searchBooks(@Param("keyword") String keyword, Pageable pageable);
}