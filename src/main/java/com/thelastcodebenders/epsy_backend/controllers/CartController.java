package com.thelastcodebenders.epsy_backend.controllers;

import com.thelastcodebenders.epsy_backend.models.dto.ApiResponse;
import com.thelastcodebenders.epsy_backend.models.dto.CartResponse;
import com.thelastcodebenders.epsy_backend.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartServcie;

    @PostMapping
    public ResponseEntity<ApiResponse<CartResponse>> addProductToCart(
            Long productId, Integer quantity
    ) {
        return ResponseEntity.ok(cartServcie.addProductToCart(productId, quantity));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getUserCart() {
        return ResponseEntity.ok(cartServcie.getUserCart());
    }
}
