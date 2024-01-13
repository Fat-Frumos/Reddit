package com.epam.esm.reddit.model.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "person")
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
    private String permissions;
    private boolean enabled;
    @Enumerated(EnumType.STRING)
    private Role roles;

    public List<String> getRoleList(){
        return !this.roles.name().isEmpty()
               ? Arrays.asList(this.roles.name().split(","))
               : new ArrayList<>();
    }

    public List<String> getPermissionList(){
        return !this.permissions.isEmpty()
               ? Arrays.asList(this.permissions.split(","))
               : new ArrayList<>();
    }
}
