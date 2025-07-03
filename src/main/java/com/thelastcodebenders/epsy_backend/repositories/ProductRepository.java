package com.thelastcodebenders.epsy_backend.repositories;

import com.thelastcodebenders.epsy_backend.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByOwnerId(UUID vendorId);
}
