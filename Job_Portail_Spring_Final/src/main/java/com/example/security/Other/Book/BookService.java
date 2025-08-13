package com.example.security.Other.Book;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.security.user.AuthorProfile.AuthorProfile;
import com.example.security.user.AuthorProfile.AuthorProfileRepository;
import com.example.security.user.PublisherProfile.PublisherProfile;
import com.example.security.user.PublisherProfile.PublisherProfileRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        fileRepository.deleteByBookId(id); // delete all files
        bookRepository.deleteById(id);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // ---------------- Search/Filter ----------------
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> getBooksByAuthor(Long authorId) {
        return bookRepository.findByAuthorId(authorId);
    }

    public List<Book> getBooksByPublisher(Long publisherId) {
        return bookRepository.findByPublisherId(publisherId);
    }

    public List<Book> getBooksByCategory(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }

    public List<Book> getBooksByFormat(String format) {
        return bookRepository.findByFormat(format);
    }

    // ---------------- Author Management ----------------
    public Book addAuthorToBook(Long bookId, Long authorId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        AuthorProfile author = authorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));
        book.getAuthors().add(author);
        return bookRepository.save(book);
    }

    public Book removeAuthorFromBook(Long bookId, Long authorId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        AuthorProfile author = authorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));
        book.getAuthors().remove(author);
        return bookRepository.save(book);
    }

    // ---------------- Category Management ----------------
    public Book addCategoryToBook(Long bookId, Long categoryId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        BookCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        book.getCategories().add(category);
        return bookRepository.save(book);
    }

    public Book removeCategoryFromBook(Long bookId, Long categoryId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        BookCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        book.getCategories().remove(category);
        return bookRepository.save(book);
    }

    public BookCategory createCategory(BookCategory category) {
        return categoryRepository.save(category);
    }

    public List<BookCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<BookCategory> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public BookCategory updateCategory(Long id, BookCategory updatedCategory) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(updatedCategory.getName());
                    return categoryRepository.save(category);
                })
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    // ---------------- Publisher ----------------
    public Book setPublisher(Long bookId, Long publisherId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        PublisherProfile publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found"));
        book.setPublisher(publisher);
        return bookRepository.save(book);
    }
    public List<BookFile> getBookFiles(Long bookId) {
        return fileRepository.findByBookId(bookId);
    }

    public Optional<BookFile> getBookFileByType(Long bookId, String fileType) {
        return fileRepository.findByBookIdAndType(bookId, fileType);
    }

    public BookFileDTO getBookFileDTOById(Long fileId) {
        BookFile bookFile = fileRepository.findById(fileId)
            .orElseThrow(() -> new IllegalArgumentException("File not found"));
        return new BookFileDTO(bookFile);
    }

    public void deleteBookFile(Long fileId) {
        if (!fileRepository.existsById(fileId)) {
            throw new IllegalArgumentException("File not found with id: " + fileId);
        }
        fileRepository.deleteById(fileId);
    }

  

    // public BookFile getBookCover(Long bookId) {
    //     return fileRepository.findByBookIdAndType(bookId, "COVER").orElse(null);
    // }

    public void deleteCover(Long bookId) {
        fileRepository.deleteByBookIdAndType(bookId, "COVER");
    }

    // ---------------- Helpers ----------------
    private void validateBookData(Book book) {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty())
            throw new IllegalArgumentException("Book title is required");
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty())
            throw new IllegalArgumentException("Book ISBN is required");

        Optional<Book> existing = bookRepository.findByIsbn(book.getIsbn());
        if (existing.isPresent() && !existing.get().getId().equals(book.getId()))
            throw new IllegalArgumentException("ISBN already exists");
    }

    private void validateFileType(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null)
            throw new IOException("File content type is required");

        if (!contentType.equals("application/pdf") &&
            !contentType.equals("application/epub+zip") &&
            !contentType.equals("text/plain") &&
            !contentType.equals("audio/mpeg") &&
            !contentType.equals("audio/mp4") &&
            !contentType.startsWith("image/")) {
            throw new IOException("Unsupported file type. Allowed: PDF, EPUB, TXT, MP3, MP4, images");
        }
    }

    private void validateImageFile(MultipartFile file) throws IOException {
        if (!file.getContentType().startsWith("image/"))
            throw new IOException("File must be an image");
        if (file.getSize() > 5 * 1024 * 1024)
            throw new IOException("Image must be <5MB");
    }



    // Upload book file
public BookFile uploadBookFile(Long bookId, MultipartFile file) throws IOException {
    Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new IllegalArgumentException("Book not found"));

    validateFileType(file);

    BookFile bookFile = new BookFile();
    bookFile.setFileName(file.getOriginalFilename());
    bookFile.setFileType(file.getContentType());
    bookFile.setData(file.getBytes()); // store as BLOB
    bookFile.setType("PDF"); // or detect dynamically
    bookFile.setBook(book);

    return fileRepository.save(bookFile);
}

// Upload cover
public BookFile uploadCover(Long bookId, MultipartFile coverFile) throws IOException {
    Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new IllegalArgumentException("Book not found"));

    validateImageFile(coverFile);

    // Delete existing cover
    fileRepository.deleteByBookIdAndType(bookId, "COVER");

    BookFile cover = new BookFile();
    cover.setFileName(coverFile.getOriginalFilename());
    cover.setFileType(coverFile.getContentType());
    cover.setData(coverFile.getBytes());
    cover.setType("COVER");
    cover.setBook(book);

    return fileRepository.save(cover);
}

// Get cover
public BookFile getBookCover(Long bookId) {
    return fileRepository.findByBookIdAndType(bookId, "COVER")
            .orElse(null);
}

}
