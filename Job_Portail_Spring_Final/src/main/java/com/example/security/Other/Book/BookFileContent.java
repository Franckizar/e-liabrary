package com.example.security.Other.Book;

// import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// @AllArgsConstructor
public class BookFileContent {
    private String fileName;
    private String fileType;
    private byte[] content;

    public BookFileContent(String fileName, String fileType, byte[] content) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.content = content;
    }

    public String getFileName() { return fileName; }
    public String getFileType() { return fileType; }
    public byte[] getContent() { return content; }
}
