package com.example.security.user.PublisherProfile;


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
@Table(name = "publisher_profiles")
public class PublisherProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    private String companyName;
    private String website;

        @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL)
        @Builder.Default
    private Set<Book> books = new HashSet<>();
}
