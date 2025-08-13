package com.example.security.Other.Book;

import jakarta.persistence.*;

@Entity
public class BookFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType;

    @Lob
    private byte[] data; // store file content in SQL as BLOB

    private String type; // e.g., "COVER", "PDF"

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
}
