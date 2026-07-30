package com.telemtry.telemetryserver.user.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class PersonalAccessToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String publicId;

    private String tokenName;

    private String tokenHash;

    private LocalDateTime creationDate;

    private LocalDateTime expirationDateAndTime;

    private Boolean revoked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @PrePersist
    void inti(){

        creationDate = LocalDateTime.now();

        publicId = UUID.randomUUID().toString().replace("-","").substring(10);

    }


}
