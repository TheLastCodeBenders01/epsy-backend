package com.thelastcodebenders.epsy_backend.models.entities;

import com.thelastcodebenders.epsy_backend.models.types.AuditableEntity;
import com.thelastcodebenders.epsy_backend.models.types.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
@Table(name = "orders")
public class Order extends AuditableEntity {
    @Id
    @Builder.Default private UUID orderId = UUID.randomUUID();

    private UUID destination; // userId of the customer
    private UUID source; // userId of the vendor

    private OrderStatus status;
    private boolean paid;

    @Transient
    private double totalAmount;
}

// orderId, destination(userId of the customer), source(userId of the vendor), status, createdAt, updatedAt, paid
