package com.thelastcodebenders.epsy_backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thelastcodebenders.epsy_backend.models.dto.ApiResponse;
import com.thelastcodebenders.epsy_backend.models.dto.PaginatedRequest;
import com.thelastcodebenders.epsy_backend.models.dto.PaginatedResponse;
import com.thelastcodebenders.epsy_backend.models.dto.ProductRequest;
import com.thelastcodebenders.epsy_backend.models.dto.ProductResponse;
import com.thelastcodebenders.epsy_backend.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>> getProducts(
            PaginatedRequest paginatedRequest
    ) {
        return ResponseEntity.ok(productService.getProducts(paginatedRequest));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @RequestBody ProductRequest request
    ) throws JsonProcessingException {
        return ResponseEntity.ok(productService.createProduct(request));
    }


    @GetMapping("vendor")
    public  ResponseEntity<ApiResponse<List<ProductResponse>>> getAllVendorProducts(
            UUID vendorId
    ) {
        return ResponseEntity.ok(productService.getAllVendorProducts(vendorId));
    }
}
