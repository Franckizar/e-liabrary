package com.example.security.user;

import com.example.security.user.Admin.AdminProfile;
import com.example.security.user.AuthorProfile.AuthorProfile;
import com.example.security.user.PublisherProfile.PublisherProfile;
import com.example.security.user.Reader.ReaderProfile;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore // Prevent password from being serialized
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // ✅ Single-role model

    @Builder.Default
    @Column(name = "token_version", columnDefinition = "int default 0")
    private Integer tokenVersion = 0;

    @Builder.Default
    private boolean emailVerified = false;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    // ======== Profile Relationships ========

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonBackReference
    private AdminProfile adminProfile;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonBackReference
    private ReaderProfile readerProfile;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonBackReference
    private AuthorProfile authorProfile;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonBackReference
    private PublisherProfile publisherProfile;

    // ======== Subscription / Plans ========
    @Column(name = "is_free_subscribed", nullable = false)
    private boolean isFreeSubscribed;

    @Column(name = "is_standard_subscribed", nullable = false)
    private boolean isStandardSubscribed;

    @Column(name = "is_premium_subscribed", nullable = false)
    private boolean isPremiumSubscribed;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SubscriptionPlanType currentPlan = SubscriptionPlanType.FREE;

    @Column(name = "subscription_expires_at")
    private LocalDateTime subscriptionExpiresAt;

    // ======== Utility Methods ========

    public void logout() {
        this.tokenVersion = 0;
    }

    public void incrementTokenVersion() {
        this.tokenVersion = (this.tokenVersion == null) ? 1 : this.tokenVersion + 1;
    }

    // ======== Spring Security Integration ========
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Single role → one authority
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @JsonIgnore
    @Override
    public String getUsername() { return email; }

    @JsonIgnore
    @Override
    public String getPassword() { return password; }

    @JsonIgnore
    @Override public boolean isAccountNonExpired() { return true; }

    @JsonIgnore
    @Override public boolean isAccountNonLocked() { return true; }

    @JsonIgnore
    @Override public boolean isCredentialsNonExpired() { return true; }

    @JsonIgnore
    @Override public boolean isEnabled() { return true; }

    // ======== Subscription Plan Enum ========
    public enum SubscriptionPlanType {
        FREE(0.0, 5, "Basic features"),
        STANDARD(5.0, 50, "Enhanced features"),
        PREMIUM(15.0, -1, "All features + priority");

        private final Double monthlyPrice;
        private final Integer applicationLimit;
        private final String description;

        SubscriptionPlanType(Double monthlyPrice, Integer applicationLimit, String description) {
            this.monthlyPrice = monthlyPrice;
            this.applicationLimit = applicationLimit;
            this.description = description;
        }

        public Double getMonthlyPrice() { return monthlyPrice; }
        public Integer getApplicationLimit() { return applicationLimit; }
        public String getDescription() { return description; }
    }
}
