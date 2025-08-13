package com.example.security.Other.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookFileRepository extends JpaRepository<BookFile, Long> {
    List<BookFile> findByBookId(Long bookId);
    Optional<BookFile> findByBookIdAndFileType(Long bookId, String fileType);
    List<BookFile> findByFileType(String fileType);
}