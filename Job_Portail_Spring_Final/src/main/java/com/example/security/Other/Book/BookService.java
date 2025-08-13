package com.example.security.Other.Book;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.security.user.AuthorProfile.AuthorProfile;
import com.example.security.user.AuthorProfile.AuthorProfileRepository;
import com.example.security.user.PublisherProfile.PublisherProfile;
import com.example.security.user.PublisherProfile.PublisherProfileRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final BookCategoryRepository categoryRepository;
    private final BookFileRepository fileRepository;
    private final AuthorProfileRepository authorRepository;
    private final PublisherProfileRepository publisherRepository;

    public BookService(BookRepository bookRepository,
                       BookCategoryRepository categoryRepository,
                       BookFileRepository fileRepository,
                       AuthorProfileRepository authorRepository,
                       PublisherProfileRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.fileRepository = fileRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    // ---------------- Book CRUD ----------------
    public Book createBook(Book book) {
        validateBookData(book);
        return bookRepository.save(book);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book updateBook(Long id, Book updatedBook) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setIsbn(updatedBook.getIsbn());
                    book.setDescription(updatedBook.getDescription());
                    book.setPrice(updatedBook.getPrice());
                    book.setFormat(updatedBook.getFormat());
                    book.setPublisher(updatedBook.getPublisher());
                    book.setAuthors(updatedBook.getAuthors());
                    book.setCategories(updatedBook.getCategories());
                    return bookRepository.save(book);
                })
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    // ---------------- File Management ----------------
    public BookFile uploadBookFile(Long bookId, MultipartFile file) throws IOException {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        validateFileType(file);

        BookFile bookFile = new BookFile();
        bookFile.setFileName(file.getOriginalFilename());
        bookFile.setFileType(file.getContentType());
        bookFile.setContent(file.getBytes());

        // Extract text from PDF/EPUB using Apache Tika
        Tika tika = new Tika();
        try {
            String extractedText = tika.parseToString(new ByteArrayInputStream(file.getBytes()));
            bookFile.setExtractedText(extractedText);
        } catch (TikaException e) {
            bookFile.setExtractedText("");
            e.printStackTrace();
        }

        bookFile.setBook(book);
        return fileRepository.save(bookFile);
    }

    public List<BookFile> getBookFiles(Long bookId) {
        return fileRepository.findByBookId(bookId);
    }

    public Optional<BookFile> getBookFileByType(Long bookId, String fileType) {
        return fileRepository.findByBookIdAndFileType(bookId, fileType);
    }

    public BookFileDTO getBookFileDTOById(Long fileId) {
        BookFile bookFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("File not found"));
        return new BookFileDTO(bookFile);
    }

    public void deleteBookFile(Long fileId) {
        if (!fileRepository.existsById(fileId)) {
            throw new IllegalArgumentException("File not found");
        }
        fileRepository.deleteById(fileId);
    }

    // ---------------- Helper Methods ----------------
    private void validateBookData(Book book) {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty())
            throw new IllegalArgumentException("Book title is required");
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty())
            throw new IllegalArgumentException("Book ISBN is required");
        if (bookRepository.findByIsbn(book.getIsbn()).isPresent())
            throw new IllegalArgumentException("Book with this ISBN already exists");
    }

    private void validateFileType(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null) throw new IOException("File content type is required");

        if (!contentType.equals("application/pdf") &&
            !contentType.equals("application/epub+zip") &&
            !contentType.equals("text/plain") &&
            !contentType.equals("audio/mpeg") &&
            !contentType.equals("audio/mp4")) {
            throw new IOException("Unsupported file type. Allowed: PDF, EPUB, TXT, MP3, MP4");
        }
    }

public BookFileContent getBookFileContentById(Long fileId) {
    BookFile bookFile = fileRepository.findById(fileId)
            .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + fileId));

    // Return the content directly from DB
    return new BookFileContent(
            bookFile.getFileName(),
            bookFile.getFileType(),
            bookFile.getContent()  // directly use byte[] from DB
    );
}


}
