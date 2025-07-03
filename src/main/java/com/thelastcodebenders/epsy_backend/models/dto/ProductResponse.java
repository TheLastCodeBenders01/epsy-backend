package com.thelastcodebenders.epsy_backend.models.dto;

import com.thelastcodebenders.epsy_backend.models.entities.ProductImage;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ProductResponse {
    private long productId;

    private String name;
    private String description;
    private double price;
    private String categories; // list of categories as a comma-separated string
    private int quantity;

    private UUID ownerId;

    private List<ProductImage> productImages;
}
