package com.example.security.user.AuthorProfile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorProfileRepository extends JpaRepository<AuthorProfile, Long> {
    Optional<AuthorProfile> findByUserId(Long userId);
}
