// package com.example.security.Other.Book;

// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.multipart.MultipartFile;

// import com.example.security.user.AuthorProfile.AuthorProfile;
// import com.example.security.user.AuthorProfile.AuthorProfileRepository;
// import com.example.security.user.PublisherProfile.PublisherProfileRepository;

// import java.io.IOException;
// import java.util.List;
// import java.util.Optional;
// import java.util.Set;
// import java.util.stream.Collectors;

// @Service
// @Transactional
// public class BookService {

//     private final BookRepository bookRepository;
//     private final BookCategoryRepository categoryRepository;
//     private final BookFileRepository fileRepository;
//     private final AuthorProfileRepository authorRepository;
//     private final PublisherProfileRepository publisherRepository;

//     public BookService(BookRepository bookRepository,
//                        BookCategoryRepository categoryRepository,
//                        BookFileRepository fileRepository,
//                        AuthorProfileRepository authorRepository,
//                        PublisherProfileRepository publisherRepository) {
//         this.bookRepository = bookRepository;
//         this.categoryRepository = categoryRepository;
//         this.fileRepository = fileRepository;
//         this.authorRepository = authorRepository;
//         this.publisherRepository = publisherRepository;
//     }

//     // ---------------- Book CRUD ----------------

//     // Create book WITHOUT cover
//     public Book createBook(Book book) {
//         validateBookData(book);

//         // Authors
//         if (book.getAuthors() != null) {
//             Set<AuthorProfile> authors = book.getAuthors().stream()
//                     .filter(a -> a.getId() != null)
//                     .map(a -> authorRepository.findById(a.getId())
//                             .orElseThrow(() -> new IllegalArgumentException("Author not found: " + a.getId())))
//                     .collect(Collectors.toSet());
//             book.setAuthors(authors);
//         }

//         // Categories
//         if (book.getCategories() != null) {
//             Set<BookCategory> categories = book.getCategories().stream()
//                     .filter(c -> c.getId() != null)
//                     .map(c -> categoryRepository.findById(c.getId())
//                             .orElseThrow(() -> new IllegalArgumentException("Category not found: " + c.getId())))
//                     .collect(Collectors.toSet());
//             book.setCategories(categories);
//         }

//         // Publisher
//         if (book.getPublisher() != null && book.getPublisher().getId() != null) {
//             book.setPublisher(publisherRepository.findById(book.getPublisher().getId())
//                     .orElseThrow(() -> new IllegalArgumentException("Publisher not found: " + book.getPublisher().getId())));
//         }

//         return bookRepository.save(book);
//     }

//     public List<BookDTO> getAllBooks() {
//         return bookRepository.findAllWithFilesAuthorsCategories()
//                 .stream().map(BookDTO::new).toList();
//     }

//     public BookDTO getBookById(Long bookId) {
//         Book book = bookRepository.findByIdWithFilesAuthorsCategories(bookId)
//                 .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));
//         return new BookDTO(book);
//     }

//     public Book updateBook(Long id, Book updatedBook) {
//         Book book = bookRepository.findById(id)
//                 .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));

//         book.setTitle(updatedBook.getTitle());
//         book.setIsbn(updatedBook.getIsbn());
//         book.setDescription(updatedBook.getDescription());
//         book.setPrice(updatedBook.getPrice());
//         book.setFormat(updatedBook.getFormat());

//         // Authors
//         if (updatedBook.getAuthors() != null) {
//             Set<AuthorProfile> authors = updatedBook.getAuthors().stream()
//                     .filter(a -> a.getId() != null)
//                     .map(a -> authorRepository.findById(a.getId())
//                             .orElseThrow(() -> new IllegalArgumentException("Author not found: " + a.getId())))
//                     .collect(Collectors.toSet());
//             book.setAuthors(authors);
//         }

//         // Categories
//         if (updatedBook.getCategories() != null) {
//             Set<BookCategory> categories = updatedBook.getCategories().stream()
//                     .filter(c -> c.getId() != null)
//                     .map(c -> categoryRepository.findById(c.getId())
//                             .orElseThrow(() -> new IllegalArgumentException("Category not found: " + c.getId())))
//                     .collect(Collectors.toSet());
//             book.setCategories(categories);
//         }

//         // Publisher
//         if (updatedBook.getPublisher() != null && updatedBook.getPublisher().getId() != null) {
//             book.setPublisher(publisherRepository.findById(updatedBook.getPublisher().getId())
//                     .orElseThrow(() -> new IllegalArgumentException("Publisher not found: " + updatedBook.getPublisher().getId())));
//         }

//         return bookRepository.save(book);
//     }

//     public void deleteBook(Long id) {
//         if (!bookRepository.existsById(id)) {
//             throw new IllegalArgumentException("Book not found: " + id);
//         }
//         bookRepository.deleteById(id);
//     }

//     // ---------------- Cover Management ----------------

//     // public BookFile uploadCover(Long bookId, MultipartFile coverFile) throws IOException {
//     //     Book book = bookRepository.findById(bookId)
//     //             .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));

//     //     validateImageFile(coverFile);

//     //     // Delete existing cover
//     //     fileRepository.deleteByBookIdAndType(bookId, "COVER");

//     //     BookFile cover = new BookFile();
//     //     cover.setBook(book);
//     //     cover.setType("COVER");
//     //     cover.setFileName(coverFile.getOriginalFilename());
//     //     cover.setFileType(coverFile.getContentType());
//     //     cover.setData(coverFile.getBytes());

//     //     return fileRepository.save(cover);
//     // }

//     public BookFile getCover(Long bookId) {
//         return fileRepository.findByBookIdAndType(bookId, "COVER").orElse(null);
//     }

//     public void deleteCover(Long bookId) {
//         fileRepository.deleteByBookIdAndType(bookId, "COVER");
//     }

//     // ---------------- Helper Methods ----------------

//     private void validateBookData(Book book) {
//         if (book.getTitle() == null || book.getTitle().trim().isEmpty())
//             throw new IllegalArgumentException("Book title is required");
//         if (book.getIsbn() == null || book.getIsbn().trim().isEmpty())
//             throw new IllegalArgumentException("Book ISBN is required");

//         Optional<Book> existing = bookRepository.findByIsbn(book.getIsbn());
//         if (existing.isPresent() && !existing.get().getId().equals(book.getId()))
//             throw new IllegalArgumentException("ISBN already exists");
//     }

//     private void validateImageFile(MultipartFile file) throws IOException {
//         String contentType = file.getContentType();
//         if (contentType == null || !contentType.startsWith("image/"))
//             throw new IOException("File must be an image");
//         if (file.getSize() > 5 * 1024 * 1024)
//             throw new IOException("Image must be less than 5MB");
//     }



//     // ---------------- Cover Management ----------------
// @Transactional
// public BookFile uploadCover(Long bookId, MultipartFile coverFile) throws IOException {
//     Book book = bookRepository.findById(bookId)
//             .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));

//     // Validate image
//     validateImageFile(coverFile);

//     // Delete existing cover
//     fileRepository.deleteByBookIdAndType(bookId, "COVER");

//     // Save new cover
//     BookFile cover = new BookFile();
//     cover.setFileName(coverFile.getOriginalFilename());
//     cover.setFileType(coverFile.getContentType());
//     cover.setData(coverFile.getBytes());
//     cover.setType("COVER");
//     cover.setBook(book);

//     return fileRepository.save(cover);
// }

// @Transactional(readOnly = true)
// public BookFile getBookCover(Long bookId) {
//     return fileRepository.findByBookIdAndType(bookId, "COVER")
//             .orElse(null);
// }

// // @Transactional
// // public void deleteCover(Long bookId) {
// //     fileRepository.deleteByBookIdAndType(bookId, "COVER");
// // }

// }
