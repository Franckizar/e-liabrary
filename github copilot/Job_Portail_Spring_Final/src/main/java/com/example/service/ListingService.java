package com.example.service;

import com.example.model.Listing;
import com.example.repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListingService {

    private final ListingRepository listingRepository;

    @Autowired
    public ListingService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public Listing createListing(Listing listing) {
        return listingRepository.save(listing);
    }

    public Optional<Listing> getListingById(Long id) {
        return listingRepository.findById(id);
    }

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    public Listing updateListing(Long id, Listing updatedListing) {
        if (listingRepository.existsById(id)) {
            updatedListing.setId(id);
            return listingRepository.save(updatedListing);
        }
        return null;
    }

    public void deleteListing(Long id) {
        listingRepository.deleteById(id);
    }

    public List<Listing> getListingsByUserId(Long userId) {
        return listingRepository.findByUserId(userId);
    }
}