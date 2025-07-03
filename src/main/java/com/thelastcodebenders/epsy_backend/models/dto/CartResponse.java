package com.thelastcodebenders.epsy_backend.models.dto;

import com.thelastcodebenders.epsy_backend.models.entities.CartProduct;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CartResponse {
    private UUID cartId;
    private List<CartProduct> products;
    private Double totalAmount;
}
