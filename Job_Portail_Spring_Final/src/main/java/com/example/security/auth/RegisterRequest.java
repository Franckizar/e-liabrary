package com.example.security.auth;

import com.example.security.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    // Generic user fields
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;
    private String profileImageUrl;

    // ================= Role-specific fields =================

    // ADMIN
    private String officeTitle;
    private String notes;

    // READER
    private String displayName;
    private String favoriteGenre;

    // AUTHOR
    private String penName;
    private String biography;

    // PUBLISHER
    private String companyName;
    private String website;
}
