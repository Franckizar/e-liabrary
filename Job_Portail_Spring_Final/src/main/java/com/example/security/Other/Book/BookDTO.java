package com.example.security.Other.Book;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.security.user.User;
import com.example.security.user.AuthorProfile.AuthorProfile;

public class BookDTO {

    private Long id;
    private String title;
    private String isbn;
    private String description;
    private BigDecimal price;
    private String format;
    private String coverFileName;
    private String coverFileType;
    private String coverImageBase64;
    private Long coverFileId;
    private Set<String> authors = new HashSet<>();
    private Set<String> categories = new HashSet<>();
    private String publisherName;

    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.isbn = book.getIsbn();
        this.description = book.getDescription();
        this.price = book.getPrice();
        this.format = book.getFormat();

        // Publisher
        if (book.getPublisher() != null) {
            this.publisherName = book.getPublisher().getCompanyName();
        }

        // Cover file
        if (book.getFiles() != null) {
            book.getFiles().stream()
                .filter(f -> "COVER".equals(f.getType()))
                .findFirst()
                .ifPresent(f -> {
                    this.coverFileName = f.getFileName();
                    this.coverFileType = f.getFileType();
                    this.coverFileId = f.getId();
                    if (f.getData() != null) {
                        this.coverImageBase64 = Base64.getEncoder().encodeToString(f.getData());
                    }
                });
        }

        // Authors
        if (book.getAuthors() != null) {
            this.authors = book.getAuthors().stream()
                    .map(this::getAuthorName)
                    .collect(Collectors.toSet());
        }

        // Categories
        if (book.getCategories() != null) {
            this.categories = book.getCategories().stream()
                    .filter(c -> c != null && c.getName() != null)
                    .map(c -> c.getName())
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

    // ---------- Getters and setters ----------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public String getCoverFileName() { return coverFileName; }
    public void setCoverFileName(String coverFileName) { this.coverFileName = coverFileName; }
    public String getCoverFileType() { return coverFileType; }
    public void setCoverFileType(String coverFileType) { this.coverFileType = coverFileType; }
    public String getCoverImageBase64() { return coverImageBase64; }
    public void setCoverImageBase64(String coverImageBase64) { this.coverImageBase64 = coverImageBase64; }
    public Long getCoverFileId() { return coverFileId; }
    public void setCoverFileId(Long coverFileId) { this.coverFileId = coverFileId; }
    public Set<String> getAuthors() { return authors; }
    public void setAuthors(Set<String> authors) { this.authors = authors; }
    public Set<String> getCategories() { return categories; }
    public void setCategories(Set<String> categories) { this.categories = categories; }
    public String getPublisherName() { return publisherName; }
    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }
}
