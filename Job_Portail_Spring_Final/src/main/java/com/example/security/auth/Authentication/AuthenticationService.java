package com.example.security.auth.Authentication;

import com.example.security.PasswordResetTokenRepository;
import com.example.security.UserRepository;
import com.example.security.auth.AuthenticationRequest;
import com.example.security.auth.AuthenticationResponse;
import com.example.security.auth.RegisterRequest;
import com.example.security.config.EmailService;
import com.example.security.config.JwtService;
import com.example.security.user.*;
import com.example.security.user.Admin.AdminProfile;
import com.example.security.user.Admin.AdminProfileRepository;
import com.example.security.user.AuthorProfile.AuthorProfile;
import com.example.security.user.AuthorProfile.AuthorProfileRepository;
import com.example.security.user.PublisherProfile.PublisherProfile;
import com.example.security.user.PublisherProfile.PublisherProfileRepository;
import com.example.security.user.Reader.ReaderProfile;
import com.example.security.user.Reader.ReaderProfileRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    private final AdminProfileRepository adminProfileRepository;
    private final ReaderProfileRepository readerProfileRepository;
    private final AuthorProfileRepository authorProfileRepository;
    private final PublisherProfileRepository publisherProfileRepository;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        boolean isFirstUser = userRepository.count() == 0;

        // Prevent multiple admins
        if (request.getRole() == Role.ADMIN && !isFirstUser) {
            long existingAdmins = userRepository.countByRole(Role.ADMIN);
            if (existingAdmins > 0) {
                throw new IllegalStateException("An admin already exists. Only one admin is allowed.");
            }
        }

        // Create User
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .tokenVersion(0)
                .role(isFirstUser ? Role.ADMIN : request.getRole())
                .build();

        user.incrementTokenVersion();
        userRepository.save(user);

        // Create profile depending on role
        switch (user.getRole()) {
            case ADMIN -> {
                AdminProfile profile = AdminProfile.builder()
                        .user(user)
                        .officeTitle(request.getOfficeTitle())
                        .notes(request.getNotes())
                        .build();
                adminProfileRepository.save(profile);
            }
            case READER -> {
                ReaderProfile profile = ReaderProfile.builder()
                        .user(user)
                        .displayName(request.getDisplayName())
                        .favoriteGenre(request.getFavoriteGenre())
                        .build();
                readerProfileRepository.save(profile);
            }
            case AUTHOR -> {
                AuthorProfile profile = AuthorProfile.builder()
                        .user(user)
                        .penName(request.getPenName())
                        .biography(request.getBiography())
                        .build();
                authorProfileRepository.save(profile);
            }
            case PUBLISHER -> {
                PublisherProfile profile = PublisherProfile.builder()
                        .user(user)
                        .companyName(request.getCompanyName())
                        .website(request.getWebsite())
                        .build();
                publisherProfileRepository.save(profile);
            }
            default -> throw new IllegalArgumentException("Unsupported role: " + user.getRole());
        }

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.incrementTokenVersion();
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public String initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<PasswordResetToken> existingTokenOpt = passwordResetTokenRepository.findByUserId(user.getId());

        String token = UUID.randomUUID().toString();

        if (existingTokenOpt.isPresent()) {
            PasswordResetToken existingToken = existingTokenOpt.get();
            existingToken.setToken(token);
            existingToken.setExpiryDate(LocalDateTime.now().plusHours(1));
            passwordResetTokenRepository.save(existingToken);
        } else {
            PasswordResetToken resetToken = PasswordResetToken.builder()
                    .token(token)
                    .user(user)
                    .expiryDate(LocalDateTime.now().plusHours(1))
                    .build();
            passwordResetTokenRepository.save(resetToken);
        }

        String resetLink = "https://yourdomain.com/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(email, resetLink);

        return "Reset email sent";
    }

    public String finalizePasswordReset(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            passwordResetTokenRepository.delete(resetToken);
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();
        user.incrementTokenVersion();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
        return "Password updated successfully";
    }

    public void logout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.logout();
        userRepository.save(user);
    }
    public User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElse(null);
}

}
