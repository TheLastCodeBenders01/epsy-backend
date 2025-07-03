package com.thelastcodebenders.epsy_backend.controllers;

import com.thelastcodebenders.epsy_backend.models.dto.ApiResponse;
import com.thelastcodebenders.epsy_backend.models.dto.VendorResponse;
import com.thelastcodebenders.epsy_backend.models.types.VendorCategory;
import com.thelastcodebenders.epsy_backend.services.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/vendors")
public class VendorController {
    private final VendorService vendorService;

    @GetMapping("categories")
    public ResponseEntity<ApiResponse<List<VendorCategory>>> getAllVendorCategories() {
        return ResponseEntity.ok(vendorService.getAllVendorCategories());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VendorResponse>>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }

    @GetMapping("by-category")
    public ResponseEntity<ApiResponse<List<VendorResponse>>> getVendorsByCategory(
            @RequestParam(name = "category") VendorCategory category
    ) {
        return ResponseEntity.ok(vendorService.getVendorsByCategory(category));
    }
}
