package com.thelastcodebenders.epsy_backend.repositories;

import com.thelastcodebenders.epsy_backend.models.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}
