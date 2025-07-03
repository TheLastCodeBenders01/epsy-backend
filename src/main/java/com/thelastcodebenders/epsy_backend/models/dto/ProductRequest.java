package com.thelastcodebenders.epsy_backend.models.dto;

import com.thelastcodebenders.epsy_backend.models.types.VendorCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductRequest {
    private String name;
    private String description;

    private double price;

    @NotNull(message = "categories must not be null")
    @Size(min = 1, message = "categories list must contain at least one category")
    private List<VendorCategory> categories;

    @NotNull(message = "images must not be null")
    @Size(min = 1, message = "images list must contain at least one category")
    private List<String> imageUrls;

    @Builder.Default private int quantity = 1;
}
