package com.example.security.Other.Book;

// Add these methods to your BookFileRepository interface

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookFileRepository extends JpaRepository<BookFile, Long> {

    // Existing method
    List<BookFile> findByBookId(Long bookId);

    // Find file by book ID and type (e.g., COVER)
     Optional<BookFile> findByBook_IdAndType(Long bookId, String type);

    // Find all files by type
    List<BookFile> findByType(String type);

    // Delete files by book ID and type
    @Modifying
    @Query("DELETE FROM BookFile f WHERE f.book.id = :bookId AND f.type = :type")
    void deleteByBookIdAndType(@Param("bookId") Long bookId, @Param("type") String type);

    // Delete all files for a book
    @Modifying
    @Query("DELETE FROM BookFile f WHERE f.book.id = :bookId")
    void deleteByBookId(@Param("bookId") Long bookId);

    // Count files by book ID
    long countByBookId(Long bookId);

    // Count files by book ID and type
    long countByBookIdAndType(Long bookId, String type);

    // Find files by book ID excluding certain types
    @Query("SELECT f FROM BookFile f WHERE f.book.id = :bookId AND f.type NOT IN :excludeTypes")
    List<BookFile> findByBookIdExcludingTypes(@Param("bookId") Long bookId, @Param("excludeTypes") List<String> excludeTypes);

        // List<BookFile> findByBookId(Long bookId);

    // Optional<BookFile> findByBookIdAndType(Long bookId, String type); // use "type" here

//     @Modifying
//     @Query("DELETE FROM BookFile f WHERE f.book.id = :bookId AND f.type = :type")
//     void deleteByBookIdAndType(@Param("bookId") Long bookId, @Param("type") String type);
}