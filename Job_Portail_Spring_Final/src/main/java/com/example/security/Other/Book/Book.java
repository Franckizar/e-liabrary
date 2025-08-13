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

    private BigDecimal originalPrice; // new field

    private String format; // e.g., Hardcover, Paperback, Ebook, Audio

    private boolean isNew = false;        // new field
    private boolean isBestseller = false; // new field
    private double rating = 0.0;          // new field
    private int reviewCount = 0;          // new field

    // Book ↔ Publisher
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private PublisherProfile publisher;

    // Book ↔ Author
    @ManyToMany
    @JoinTable(
        name = "book_author",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<AuthorProfile> authors = new HashSet<>();

    // Book ↔ Category
    @ManyToMany
    @JoinTable(
        name = "book_category",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<BookCategory> categories = new HashSet<>();

    // Book ↔ File
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookFile> files = new HashSet<>();

    // Helper method to get cover file
    public BookFile getCover() {
        return files.stream()
                .filter(f -> "COVER".equals(f.getType()))
                .findFirst()
                .orElse(null);
    }

    // Helper to get main genre (first category name)
    public String getMainGenre() {
        return categories.stream()
                .findFirst()
                .map(BookCategory::getName)
                .orElse("Unknown");
    }

    // Manual setters for boolean fields so BookService can call setIsNew/setIsBestseller
    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public void setIsBestseller(boolean isBestseller) {
        this.isBestseller = isBestseller;
    }
}
