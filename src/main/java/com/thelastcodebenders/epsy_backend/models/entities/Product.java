package com.thelastcodebenders.epsy_backend.models.entities;

import com.thelastcodebenders.epsy_backend.models.types.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "products")
public class Product extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;

    private String name;
    private String description;
    private String imageUrl;
    private double price;
    private String categories; // list of categories as a comma-separated string

    @Column(nullable = false)
    @Builder.Default private int quantity = 1;

    private UUID ownerId;
}

// productId, categories, name, description, imageUrl, price, quantity, ownerId, createdAt, updatedAt,
