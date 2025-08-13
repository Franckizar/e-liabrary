package com.example.security.Other.Book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.example.security.user.AuthorProfile.AuthorProfile;
import com.example.security.user.PublisherProfile.PublisherProfile;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal price;

    private String format; // e.g., Hardcover, Paperback, Ebook, Audio

    // Book ↔ Publisher (Many books can be from one publisher)
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private PublisherProfile publisher;

    // Book ↔ Author (Many-to-Many through book_author)
    @ManyToMany
    @JoinTable(
        name = "book_author",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<AuthorProfile> authors = new HashSet<>();

    // Book ↔ Category (Many-to-Many)
    @ManyToMany
    @JoinTable(
        name = "book_category",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<BookCategory> categories = new HashSet<>();

    // Book ↔ File (One-to-Many)
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookFile> files = new HashSet<>();
}
