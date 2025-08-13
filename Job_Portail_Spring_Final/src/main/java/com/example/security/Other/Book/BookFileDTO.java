package com.example.security.Other.Book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookFileDTO {
    private String fileName;
    private String fileType;
    private String fileUrl;

    public BookFileDTO(BookFile bookFile) {
        this.fileName = bookFile.getFileName();
        this.fileType = bookFile.getFileType();
      
    }

    // getters
}

