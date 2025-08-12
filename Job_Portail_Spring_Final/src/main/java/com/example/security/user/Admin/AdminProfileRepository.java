package com.example.security.user.Admin;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminProfileRepository extends JpaRepository<AdminProfile, Long> {
    Optional<AdminProfile> findByUserId(Long userId);
}
