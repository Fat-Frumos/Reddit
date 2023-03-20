package com.epam.esm.reddit.domain;

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
        if(this.roles.name().length() > 0){
            return Arrays.asList(this.roles.name().split(","));
        }
        return new ArrayList<>();
    }

    public List<String> getPermissionList(){
        if(this.permissions.length() > 0){
            return Arrays.asList(this.permissions.split(","));
        }
        return new ArrayList<>();
    }
}
