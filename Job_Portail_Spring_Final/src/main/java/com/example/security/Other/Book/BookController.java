package com.example.security.Other.Book;

import org.springframework.http.HttpHeaders;
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

    // ----------------- Book CRUD -----------------
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        try {
            Book createdBook = bookService.createBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(new BookDTO(createdBook));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(book -> ResponseEntity.ok(new BookDTO(book)))
                .orElse(ResponseEntity.notFound().build());
    }

    // @GetMapping
    // public ResponseEntity<List<BookDTO>> getAllBooks() {
    //     List<BookDTO> books = bookService.getAllBooks()
    //             .stream().map(BookDTO::new).collect(Collectors.toList());
    //     return ResponseEntity.ok(books);
    // }
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book book) {
        try {
            Book updatedBook = bookService.updateBook(id, book);
            return ResponseEntity.ok(new BookDTO(updatedBook));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok("Book deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

 // ----------------- File Management -----------------
@PostMapping(value = "/{bookId}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<?> uploadBookFile(@PathVariable Long bookId, @RequestParam("file") MultipartFile file) {
    // Log the request details
    System.out.println("[INFO] Uploading file for book ID: " + bookId);
    System.out.println("[INFO] File name: " + file.getOriginalFilename());
    System.out.println("[INFO] File content type: " + file.getContentType());
    System.out.println("[INFO] File size: " + file.getSize() + " bytes");

    try {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: Uploaded file is empty.");
        }

        BookFile bookFile = bookService.uploadBookFile(bookId, file);

        System.out.println("[INFO] File uploaded successfully. File ID: " + bookFile.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(new BookFileDTO(bookFile));
    } catch (IOException e) {
        System.err.println("[ERROR] IOException during file upload: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error uploading file: " + e.getMessage());
    } catch (IllegalArgumentException e) {
        System.err.println("[ERROR] IllegalArgumentException: " + e.getMessage());
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("[ERROR] Unexpected error: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected error during file upload: " + e.getMessage());
    }
}


    @GetMapping("/{bookId}/files")
    public ResponseEntity<List<BookFileDTO>> getBookFiles(@PathVariable Long bookId) {
        List<BookFileDTO> files = bookService.getBookFiles(bookId)
                .stream().map(BookFileDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(files);
    }
    ////////////////////////////////////here

    @GetMapping("/{bookId}/type/{fileType}")
    public ResponseEntity<?> getBookFileByType(@PathVariable Long bookId, @PathVariable String fileType) {
        return bookService.getBookFileByType(bookId, fileType)
                .map(file -> ResponseEntity.ok(new BookFileDTO(file)))
                .orElse(ResponseEntity.notFound().build());
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
            return ResponseEntity.ok("File deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // ----------------- Cover Management -----------------
    @PostMapping(value = "/{bookId}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadCover(@PathVariable Long bookId, @RequestParam("cover") MultipartFile cover) {
        try {
            BookFile coverFile = bookService.uploadCover(bookId, cover);
            return ResponseEntity.status(HttpStatus.CREATED).body(new BookFileDTO(coverFile));
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

  @GetMapping("/{bookId}/cover")
public ResponseEntity<?> getCover(@PathVariable Long bookId) {
    BookFile cover = bookService.getBookCover(bookId);
    if (cover == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Cover not found for book with ID: " + bookId);
    }

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + cover.getFileName() + "\"")
            .contentType(MediaType.parseMediaType(cover.getFileType()))
            .body(cover.getData());
}


    @DeleteMapping("/{bookId}/cover")
    public ResponseEntity<?> deleteCover(@PathVariable Long bookId) {
        bookService.deleteCover(bookId);
        return ResponseEntity.ok("Cover deleted successfully");
    }


    /////////////////////////////////////////////
    /// 
    @GetMapping("/{bookId}/files/{fileId}")
public ResponseEntity<byte[]> getBookFile(
        @PathVariable Long bookId,
        @PathVariable Long fileId) {

    try {
        System.out.println("[INFO] Fetching fileId=" + fileId + " for bookId=" + bookId);
        BookFile bookFile = bookService.getBookFileById(fileId, bookId);

        if (bookFile == null) {
            System.err.println("[WARN] File not found for fileId=" + fileId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + bookFile.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(bookFile.getFileType()))
                .body(bookFile.getData());

    } catch (IllegalArgumentException e) {
        System.err.println("[ERROR] " + e.getMessage());
        return ResponseEntity.badRequest().body(null);
    }
}

}