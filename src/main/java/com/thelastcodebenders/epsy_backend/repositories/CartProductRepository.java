package com.thelastcodebenders.epsy_backend.repositories;

import com.thelastcodebenders.epsy_backend.models.entities.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    Optional<CartProduct> findByCartIdAndProductId(UUID cartId, Long productId);
    List<CartProduct> findByCartId(UUID cartId);
}
