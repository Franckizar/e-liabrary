package com.example.service;

import com.example.model.Favorite;
import com.example.model.Listing;
import com.example.model.User;
import com.example.repository.FavoriteRepository;
import com.example.repository.ListingRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ListingRepository listingRepository;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository, ListingRepository listingRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
    }

    public Favorite addFavorite(Long userId, Long listingId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new RuntimeException("Listing not found"));

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setListing(listing);
        return favoriteRepository.save(favorite);
    }

    public void removeFavorite(Long userId, Long listingId) {
        Favorite favorite = favoriteRepository.findByUserIdAndListingId(userId, listingId)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));
        favoriteRepository.delete(favorite);
    }

    public List<Listing> getUserFavorites(Long userId) {
        return favoriteRepository.findAllByUserId(userId);
    }
}