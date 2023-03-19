package com.epam.esm.reddit.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userId;
    @Nullable
    private String username;
    @Nullable
    private String password;
    @Nullable
    private String email;
    private Instant createdDate;
    private boolean enabled;
}
