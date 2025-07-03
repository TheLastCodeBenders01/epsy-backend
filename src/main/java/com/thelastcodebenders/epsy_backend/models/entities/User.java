package com.thelastcodebenders.epsy_backend.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thelastcodebenders.epsy_backend.models.dto.VendorResponse;
import com.thelastcodebenders.epsy_backend.models.types.AuditableEntity;
import com.thelastcodebenders.epsy_backend.models.types.Role;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "users")
public class User extends AuditableEntity implements UserDetails {

    @Id
    @Builder.Default private UUID userId = UUID.randomUUID();

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    private String location;

    private String vendorCategories;

    @JsonIgnore
    private String password;

    // vendor specific details
    private Boolean isVendor;
    private String imageUrl;
    private String displayName;
    private String telegramUsername;

    @Builder.Default
    private boolean emailVerified = false;

    @Enumerated(EnumType.STRING)
    @Builder.Default private Role role = Role.USER;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    public VendorResponse toVendorResponse() {
        try {
            return VendorResponse.builder()
                    .displayName(displayName)
                    .imageUrl(imageUrl)
                    .vendorId(userId)
                    .telegramUsername(telegramUsername)
                    .vendorCategories(new ObjectMapper().readValue(vendorCategories, new TypeReference<>() {
                    }))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

// userId, firstName, lastName, email, createdAt, updatedAt, isVendor, vendorCategory, location, phoneNumber, password
