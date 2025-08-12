package com.example.model;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password_hash;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    // public User() {
    // }

    // public User(String name, String email, String password_hash, String phone, Role role, LocalDateTime created_at, LocalDateTime updated_at) {
    //     this.name = name;
    //     this.email = email;
    //     this.password_hash = password_hash;
    //     this.phone = phone;
    //     this.role = role;
    //     this.created_at = created_at;
    //     this.updated_at = updated_at;
    // }

    // // Getters and Setters

    // public Long getId() {
    //     return id;
    // }

    // public void setId(Long id) {
    //     this.id = id;
    // }

    // public String getName() {
    //     return name;
    // }

    // public void setName(String name) {
    //     this.name = name;
    // }

    // public String getEmail() {
    //     return email;
    // }

    // public void setEmail(String email) {
    //     this.email = email;
    // }

    // public String getPassword_hash() {
    //     return password_hash;
    // }

    // public void setPassword_hash(String password_hash) {
    //     this.password_hash = password_hash;
    // }

    // public String getPhone() {
    //     return phone;
    // }

    // public void setPhone(String phone) {
    //     this.phone = phone;
    // }

    // public Role getRole() {
    //     return role;
    // }

    // public void setRole(Role role) {
    //     this.role = role;
    // }

    // public LocalDateTime getCreated_at() {
    //     return created_at;
    // }

    // public void setCreated_at(LocalDateTime created_at) {
    //     this.created_at = created_at;
    // }

    // public LocalDateTime getUpdated_at() {
    //     return updated_at;
    // }

    // public void setUpdated_at(LocalDateTime updated_at) {
    //     this.updated_at = updated_at;
    // }

    public enum Role {
        USER,
        ADMIN
    }
}