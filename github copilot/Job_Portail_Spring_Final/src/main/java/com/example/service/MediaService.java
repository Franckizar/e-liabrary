package com.example.service;

import com.example.model.Media;
import com.example.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class MediaService {

    private final MediaRepository mediaRepository;

    @Autowired
    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public Media uploadMedia(MultipartFile file, Long listingId) throws IOException {
        Media media = new Media();
        media.setFilePath(file.getOriginalFilename()); // You may want to save the file to a specific location
        media.setType(determineMediaType(file));
        media.setListingId(listingId);
        media.setCreatedAt(System.currentTimeMillis()); // Set current timestamp

        return mediaRepository.save(media);
    }

    public List<Media> getMediaByListingId(Long listingId) {
        return mediaRepository.findByListingId(listingId);
    }

    private String determineMediaType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null) {
            if (contentType.startsWith("image/")) {
                return "image";
            } else if (contentType.startsWith("video/")) {
                return "video";
            }
        }
        return "unknown";
    }
}