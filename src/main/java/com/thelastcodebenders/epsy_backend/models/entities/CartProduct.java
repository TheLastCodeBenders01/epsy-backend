package com.thelastcodebenders.epsy_backend.models.entities;

import com.thelastcodebenders.epsy_backend.models.types.AuditableEntity;
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
import lombok.ToString;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@ToString(callSuper = true)
@Table(name = "cart_products")
public class CartProduct extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartProductId;

    private long productId;
    private UUID cartId;

    // extra details
    private String productName;
    private String vendorName;

    private int quantity;
    private double pricePerUnit;
}
