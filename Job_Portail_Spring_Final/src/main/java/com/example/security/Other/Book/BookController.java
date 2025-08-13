package com.example.security.Other.Book;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth/books")
@CrossOrigin(origins = "*")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // ---------------- Book CRUD ----------------
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        try {
            Book created = bookService.createBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(new BookDTO(created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(book -> ResponseEntity.ok(new BookDTO(book)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks()
                .stream().map(BookDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    // ---------------- File Management ----------------
    @PostMapping(value = "/{bookId}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadBookFile(@PathVariable Long bookId, @RequestParam("file") MultipartFile file) {
        try {
            BookFile saved = bookService.uploadBookFile(bookId, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(new BookFileDTO(saved));
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{bookId}/files")
    public ResponseEntity<List<BookFileDTO>> getBookFiles(@PathVariable Long bookId) {
        List<BookFileDTO> files = bookService.getBookFiles(bookId)
                .stream().map(BookFileDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(files);
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<BookFileDTO> getBookFile(@PathVariable Long fileId) {
        try {
            return ResponseEntity.ok(bookService.getBookFileDTOById(fileId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<?> deleteBookFile(@PathVariable Long fileId) {
        try {
            bookService.deleteBookFile(fileId);
            return ResponseEntity.ok("File deleted");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


@GetMapping("/files/download/{fileId}")
public ResponseEntity<byte[]> downloadBookFile(@PathVariable Long fileId) {
    try {
        BookFileContent fileContent = bookService.getBookFileContentById(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileContent.getFileType()))
                .header("Content-Disposition", "attachment; filename=\"" + fileContent.getFileName() + "\"")
                .body(fileContent.getContent());

    } catch (IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }
}


}
