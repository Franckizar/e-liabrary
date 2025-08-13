package com.example.security.Other.Book;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.security.user.User;
import com.example.security.user.AuthorProfile.AuthorProfile;
// import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
// import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private Long id;
    private String title;
    private String isbn;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice; // new
    private String format;
    private boolean isNew;             // new
    private boolean isBestseller;      // new
    private double rating;             // new
    private int reviewCount;           // new
    private String genre;              // new (from first category)
    private String coverFileName;
    private String coverFileType;
    private String coverImageBase64;
    private Long coverFileId;
    private String coverUrl;           // new: can be base64 URL
    private Set<String> authors = new HashSet<>();
    private Set<String> categories = new HashSet<>();
    private String publisherName;


    public void setIsNew(boolean isNew) {
    this.isNew = isNew;
}

public void setIsBestseller(boolean isBestseller) {
    this.isBestseller = isBestseller;
}


    // Custom constructor that maps from Book entity
    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.isbn = book.getIsbn();
        this.description = book.getDescription();
        this.price = book.getPrice();
        this.originalPrice = book.getOriginalPrice();
        this.format = book.getFormat();
        this.isNew = book.isNew();
        this.isBestseller = book.isBestseller();
        this.rating = book.getRating();
        this.reviewCount = book.getReviewCount();
        this.genre = book.getMainGenre();

        if (book.getPublisher() != null) {
            this.publisherName = book.getPublisher().getCompanyName();
        }

        // Cover file mapping
        if (book.getFiles() != null && !book.getFiles().isEmpty()) {
            book.getFiles().stream()
                .filter(f -> "COVER".equals(f.getType()))
                .findFirst()
                .ifPresent(f -> {
                    this.coverFileName = f.getFileName();
                    this.coverFileType = f.getFileType();
                    this.coverFileId = f.getId();
                    if (f.getData() != null) {
                        this.coverImageBase64 = Base64.getEncoder().encodeToString(f.getData());
                        this.coverUrl = "data:" + f.getFileType() + ";base64," + this.coverImageBase64;
                    }
                });
        }

        // Authors mapping
        if (book.getAuthors() != null) {
            this.authors = book.getAuthors().stream()
                    .map(this::getAuthorName)
                    .collect(Collectors.toSet());
        }

        // Categories mapping
        if (book.getCategories() != null) {
            this.categories = book.getCategories().stream()
                    .filter(c -> c != null && c.getName() != null)
                    .map(BookCategory::getName)
                    .collect(Collectors.toSet());
        }
    }

    private String getAuthorName(AuthorProfile author) {
        if (author == null) return "Unknown Author";
        if (author.getPenName() != null && !author.getPenName().trim().isEmpty()) 
            return author.getPenName();

        User user = author.getUser();
        if (user != null) {
            String name = ((user.getFirstname() != null ? user.getFirstname() : "") + " " +
                           (user.getLastname() != null ? user.getLastname() : "")).trim();
            if (!name.isEmpty()) return name;
        }
        return "Unknown Author";
    }
}
