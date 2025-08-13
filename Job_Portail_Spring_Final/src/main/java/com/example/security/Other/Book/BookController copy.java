// package com.example.security.Other.Book;

// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import java.io.IOException;
// import java.util.List;

// @RestController
// @RequestMapping("/api/v1/auth/books")
// @CrossOrigin(origins = "*")
// public class BookController {

//     private final BookService bookService;
//     private final ObjectMapper mapper = new ObjectMapper();

//     public BookController(BookService bookService) {
//         this.bookService = bookService;
//     }

//     // ---------------- Book CRUD ----------------

//     @PostMapping
//     public ResponseEntity<BookDTO> createBook(@RequestBody Book book) {
//         Book saved = bookService.createBook(book);
//         return ResponseEntity.status(201).body(new BookDTO(saved));
//     }

//     @GetMapping
//     public ResponseEntity<List<BookDTO>> getAllBooks() {
//         return ResponseEntity.ok(bookService.getAllBooks());
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<BookDTO> getBook(@PathVariable Long id) {
//         return ResponseEntity.ok(bookService.getBookById(id));
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
//         Book book = bookService.updateBook(id, updatedBook);
//         return ResponseEntity.ok(new BookDTO(book));
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<String> deleteBook(@PathVariable Long id) {
//         bookService.deleteBook(id);
//         return ResponseEntity.ok("Book deleted successfully");
//     }

//     // ---------------- Cover Management ----------------

//  @PostMapping("/{bookId}/cover")
// public ResponseEntity<?> uploadCover(@PathVariable Long bookId,
//                                      @RequestPart("cover") MultipartFile cover) {
//     try {
//         if (cover == null || cover.isEmpty()) {
//             return ResponseEntity.badRequest().body("No cover file provided or file is empty");
//         }

//         BookFile saved = bookService.uploadCover(bookId, cover);
//         return ResponseEntity.ok(new BookFileDTO(saved));

//     } catch (IllegalArgumentException e) {
//         // Book not found or other business logic exception
//         return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                              .body("Book with ID " + bookId + " not found: " + e.getMessage());
//     } catch (IOException e) {
//         // File processing error
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                              .body("Failed to process cover file: " + e.getMessage());
//     } catch (Exception e) {
//         // Catch-all for unexpected errors
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                              .body("Unexpected error: " + e.getMessage());
//     }
// }

//     // @GetMapping("/{bookId}/cover")
//     // public ResponseEntity<byte[]> getCover(@PathVariable Long bookId) {
//     //     BookFile cover = bookService.getCover(bookId);
//     //     if (cover == null) return ResponseEntity.notFound().build();

//     //     return ResponseEntity.ok()
//     //             .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + cover.getFileName() + "\"")
//     //             .contentType(MediaType.parseMediaType(cover.getFileType()))
//     //             .body(cover.getData());
//     // }

//     @DeleteMapping("/{bookId}/cover")
//     public ResponseEntity<String> deleteCover(@PathVariable Long bookId) {
//         bookService.deleteCover(bookId);
//         return ResponseEntity.ok("Cover deleted successfully");
//     }

//     @GetMapping("/{bookId}/cover")
// public ResponseEntity<byte[]> getBookCover(@PathVariable Long bookId) {
//     try {
//         BookFile cover = bookService.getBookCover(bookId);
//         if (cover == null) {
//             return ResponseEntity.notFound().build();
//         }

//         return ResponseEntity.ok()
//                 .header(HttpHeaders.CONTENT_DISPOSITION,
//                         "inline; filename=\"" + cover.getFileName() + "\"")
//                 .contentType(MediaType.parseMediaType(cover.getFileType()))
//                 .body(cover.getData());

//     } catch (Exception e) {
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                              .body(null);
//     }
// }

// }
