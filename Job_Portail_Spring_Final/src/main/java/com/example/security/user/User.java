package com.example.security.user;

import com.example.security.Other.AiJobMatch.AiJobMatch;
import com.example.security.Other.Application.Application;
import com.example.security.Other.ConversationParticipant.ConversationParticipant;
import com.example.security.Other.Message.Message;
import com.example.security.Other.Notification.Notification;
import com.example.security.Other.Payment.Payment;
import com.example.security.Other.Subscription.Subscription;
import com.example.security.Other.UserImage.UserImage;
// import com.example.security.Other.UserSkillId.UserSkill;
// import com.example.security.Other.UserSkill.UserSkill;
import com.example.security.user.Admin.Admin;
import com.example.security.user.Enterprise.Enterprise;
import com.example.security.user.JobSeeker.JobSeeker;    // Add this import for PersonalEmployer entity
import com.example.security.user.PersonalEmployerProfile.PersonalEmployerProfile;
import com.example.security.user.Technicien.Technician;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

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
    private Integer id;

    private String firstname;
    private String lastname;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Builder.Default
    @Column(name = "token_version", columnDefinition = "int default 0")
    private Integer tokenVersion = 0;

    // ================= Roles =================
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Builder.Default
    private List<Role> roles = new ArrayList<>();

    // ========== Profile Relationships ==========
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonBackReference
    private Admin adminProfile;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonBackReference
    private Technician technician;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonBackReference
    private JobSeeker jobSeekerProfile;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonBackReference
    private Enterprise enterpriseProfile;

    // ** Added PersonalEmployer Profile Relationship **
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonBackReference
    private PersonalEmployerProfile personalEmployerProfile;

    // ========== Business Relationships ==========
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserImage> userImages;

    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Application> applications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AiJobMatch> aiJobMatches;

    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<UserSkill> userSkills;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Subscription> subscriptions;

    // @OneToMany(mappedBy = "payer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Payment> paymentsMade;

    // @OneToMany(mappedBy = "payee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Payment> paymentsReceived;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Payment> payments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ConversationParticipant> conversationParticipants;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Message> messagesSent;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notification> notifications;

    // ========== Logic Methods ==========

    public void logout() {
        this.tokenVersion = 0;
    }

    public void incrementTokenVersion() {
        this.tokenVersion = (this.tokenVersion == null) ? 1 : this.tokenVersion + 1;
    }

    public void addRole(Role role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    // ========== Spring Security Integration ==========

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
    }

    @Override public String getUsername()               { return email; }
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()                { return true; }

    // ========== Profile Getters (Optional) ==========

    public JobSeeker getJobSeekerProfile()    { return jobSeekerProfile; }
    public Technician getTechnician()          { return technician; }
    public Admin getAdminProfile()              { return adminProfile; }
    public Enterprise getEnterpriseProfile()   { return enterpriseProfile; }
    public PersonalEmployerProfile getPersonalEmployerProfile() { return personalEmployerProfile; }   // Added getter

    //////////////////////////////////////////////////////////////
    
    // Subscription flags (Only one should be true at a time)
    // @Builder.Default
    @Column(name = "is_free_subscribed", nullable = false)
    private boolean isFreeSubscribed;

    @Column(name = "is_standard_subscribed", nullable = false)
    private boolean isStandardSubscribed;

    @Column(name = "is_premium_subscribed", nullable = false)
    private boolean isPremiumSubscribed;

    ////////////////////////////////////////////
    public void setIsFreeSubscribed(boolean isFreeSubscribed) {
        this.isFreeSubscribed = isFreeSubscribed;
    }
    public void setIsStandardSubscribed(boolean isStandardSubscribed) { 
        this.isStandardSubscribed = isStandardSubscribed;
    }
    public void setIsPremiumSubscribed(boolean isPremiumSubscribed) {
        this.isPremiumSubscribed = isPremiumSubscribed;
    }

    /// 

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SubscriptionPlanType currentPlan = SubscriptionPlanType.FREE;

    @Column(name = "subscription_expires_at")
    private LocalDateTime subscriptionExpiresAt;

    // Add this enum inside your User class
    public enum SubscriptionPlanType {
        FREE(0.0, 5, "Basic features"),
        STANDARD(5.0, 50, "Enhanced features"), 
        PREMIUM(15.0, -1, "All features + priority");
        // STANDARD(5000.0, 50, "Enhanced features"), 
        // PREMIUM(15000.0, -1, "All features + priority");
        
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
