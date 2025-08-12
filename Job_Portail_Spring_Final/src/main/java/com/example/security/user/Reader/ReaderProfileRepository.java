package com.example.security.user.Reader;



import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReaderProfileRepository extends JpaRepository<ReaderProfile, Long> {
    Optional<ReaderProfile> findByUserId(Long userId);
}
