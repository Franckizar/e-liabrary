package com.example.security.Other.Book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_files")
public class BookFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType; // e.g., PDF, EPUB, MP3

    @Lob
    private byte[] content;  // store the actual file content

    @Lob
    private String extractedText; // optional, like CV text for PDFs

    // Book ↔ File (Many-to-One)
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "book_id")
    private Book book;
}
