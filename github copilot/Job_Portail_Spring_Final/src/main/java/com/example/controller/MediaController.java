package com.example.controller;

import com.example.model.Media;
import com.example.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @PostMapping("/upload")
    public ResponseEntity<Media> uploadMedia(@RequestParam("file") MultipartFile file, @RequestParam("listingId") Long listingId) {
        Media media = mediaService.uploadMedia(file, listingId);
        return new ResponseEntity<>(media, HttpStatus.CREATED);
    }

    @GetMapping("/{listingId}")
    public ResponseEntity<List<Media>> getMediaByListingId(@PathVariable Long listingId) {
        List<Media> mediaList = mediaService.getMediaByListingId(listingId);
        return new ResponseEntity<>(mediaList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) {
        mediaService.deleteMedia(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}