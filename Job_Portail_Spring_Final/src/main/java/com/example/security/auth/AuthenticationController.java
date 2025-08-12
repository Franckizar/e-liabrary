package com.example.security.auth;

import com.example.security.auth.Authentication.AuthenticationService;
import com.example.security.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Register a new user in the system.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        System.out.println("[AuthenticationController] Registration attempt for: " + request.getEmail());
        try {
            AuthenticationResponse response = authenticationService.register(request);
            System.out.println("[AuthenticationController] Registration successful for: " + request.getEmail());
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            // This typically occurs if another ADMIN already exists
            System.out.println("[AuthenticationController] Admin registration blocked: " + e.getMessage());
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            System.out.println("[AuthenticationController] Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    /**
     * Authenticate a user and return a JWT token.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        System.out.println("[AuthenticationController] Received authentication request for: " + request.getEmail());
        try {
            AuthenticationResponse response = authenticationService.authenticate(request);
            System.out.println("[AuthenticationController] Authentication successful for: " + request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("[AuthenticationController] Authentication failed for " + request.getEmail() + ": " + e.getMessage());
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * Test public endpoint.
     */
    @GetMapping("/demo")
    public ResponseEntity<String> sayHello() {
        System.out.println("[AuthenticationController] Accessing demo endpoint");
        return ResponseEntity.ok("Hello from unsecured endpoint");
    }

    /**
     * Send password reset link by email.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        System.out.println("[AuthenticationController] Received password reset request for: " + email);
        try {
            String result = authenticationService.initiatePasswordReset(email);
            System.out.println("[AuthenticationController] Password reset initiated for: " + email);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            System.out.println("[AuthenticationController] Password reset failed for " + email + ": " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while processing your request.");
        }
    }

    /**
     * Complete password reset.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        System.out.println("[AuthenticationController] Received password reset confirmation for token: " + token);
        try {
            String result = authenticationService.finalizePasswordReset(token, newPassword);
            System.out.println("[AuthenticationController] Password reset completed for token: " + token);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            System.out.println("[AuthenticationController] Password reset failed for token " + token + ": " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Logout the authenticated user.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        try {
            String email = authentication.getName();
            System.out.println("[AuthenticationController] Logging out: " + email);
            authenticationService.logout(email);
            return ResponseEntity.ok("Logged out successfully");
        } catch (Exception e) {
            System.out.println("[AuthenticationController] Logout failed: " + e.getMessage());
            return ResponseEntity.status(401).body("Logout failed: " + e.getMessage());
        }
    }

    /**
     * Return all users with UNKNOWN role (rarely used in ÉdiNova but kept for admin auditing).
     */
    // @GetMapping("/users/unknown")
    // public ResponseEntity<?> getUsersWithUnknownRole() {
    //     return ResponseEntity.ok(authenticationService.getAllUnknownUsers());
    // }

    /**
     * Get currently authenticated user's details.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String email = authentication.getName();
        User user = authenticationService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        return ResponseEntity.ok(user);
    }
}
