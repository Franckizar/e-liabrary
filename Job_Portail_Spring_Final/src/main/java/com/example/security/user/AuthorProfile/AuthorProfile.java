package com.example.security.user.AuthorProfile;


import java.util.HashSet;
import java.util.Set;

import com.example.security.Other.Book.Book;
import com.example.security.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "author_profiles")
public class AuthorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    private String penName;
    @Column(length = 2000)
    private String biography;

       // Author ↔ Book (Many-to-Many)
    @ManyToMany(mappedBy = "authors")
    @Builder.Default
    private Set<Book> books = new HashSet<>();
}
