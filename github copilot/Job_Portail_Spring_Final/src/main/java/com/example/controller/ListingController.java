package com.example.controller;

import com.example.model.Listing;
import com.example.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listings")
public class ListingController {

    @Autowired
    private ListingService listingService;

    @GetMapping
    public ResponseEntity<List<Listing>> getAllListings() {
        List<Listing> listings = listingService.getAllListings();
        return ResponseEntity.ok(listings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Listing> getListingById(@PathVariable("id") Integer id) {
        Listing listing = listingService.getListingById(id);
        return listing != null ? ResponseEntity.ok(listing) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Listing> createListing(@RequestBody Listing listing) {
        Listing createdListing = listingService.createListing(listing);
        return ResponseEntity.status(201).body(createdListing);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Listing> updateListing(@PathVariable("id") Integer id, @RequestBody Listing listing) {
        Listing updatedListing = listingService.updateListing(id, listing);
        return updatedListing != null ? ResponseEntity.ok(updatedListing) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable("id") Integer id) {
        boolean isDeleted = listingService.deleteListing(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}