package com.example.security.Other.Book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book_categories")
public class BookCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., Fiction, Science, Romance

    // Category ↔ Book (Many-to-Many)
    @ManyToMany(mappedBy = "categories")
    private Set<Book> books = new HashSet<>();

    // Getters & Setters
    // ...
}
