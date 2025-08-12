package com.example.security.user.PublisherProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PublisherProfileRepository extends JpaRepository<PublisherProfile, Long> {
    
    // Custom finder example: find by linked user ID
    Optional<PublisherProfile> findByUserId(Long userId);
}
