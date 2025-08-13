package com.example.security.Other.Book;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// BookDTO.java

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private String description;
    private String isbn;
    private String format;
    private BigDecimal price;

    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.isbn = book.getIsbn();
        this.format = book.getFormat();
        this.price = book.getPrice();
    }

    // Getters only (no setters needed for read-only DTO)
}
