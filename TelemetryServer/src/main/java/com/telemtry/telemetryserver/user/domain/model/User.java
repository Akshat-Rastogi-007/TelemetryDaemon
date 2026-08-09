package com.telemtry.telemetryserver.user.domain.model;

import com.telemtry.telemetryserver.user.application.user.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String publicId;

    @NotNull
    private String firstName;

    private String lastName;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    private List<Roles> roles = new ArrayList<>();

    private AccountStatus accountStatus;

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<PersonalAccessToken> tokens = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @PrePersist
    public void generateId() {
        if (this.publicId == null) {
            this.publicId = "user_" + UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        }
    }

}

