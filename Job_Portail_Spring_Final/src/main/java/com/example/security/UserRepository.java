package com.example.security;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.security.user.Role;
import com.example.security.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    long countByRole(Role role);
}
