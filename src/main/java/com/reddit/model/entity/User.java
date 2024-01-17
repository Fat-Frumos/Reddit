package com.reddit.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "person")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userId;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    private Instant createdDate;
    private String permissions;
    private boolean enabled;
    @Enumerated(EnumType.STRING)
    private Role role;

    public List<String> getRoleList(){
        return this.role != null && !this.role.name().isEmpty()
               ? Arrays.asList(this.role.name().split(","))
               : new ArrayList<>();
    }

    public List<String> getPermissionList() {
        return this.permissions != null && !this.permissions.isEmpty()
               ? Arrays.asList(this.permissions.split(","))
               : new ArrayList<>();
    }
}
