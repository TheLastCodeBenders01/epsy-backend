package com.thelastcodebenders.epsy_backend.models.entities;

import com.thelastcodebenders.epsy_backend.models.types.AuditableEntity;
import com.thelastcodebenders.epsy_backend.models.types.OtpPurpose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "otps")
public class Otp extends AuditableEntity {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    @Builder.Default
    private boolean used = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OtpPurpose purpose;

    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }
}
