package com.thelastcodebenders.epsy_backend.services;

import com.thelastcodebenders.epsy_backend.models.dto.ApiResponse;
import com.thelastcodebenders.epsy_backend.models.dto.VendorResponse;
import com.thelastcodebenders.epsy_backend.models.types.VendorCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VendorService {
    private final UserService userService;
    public ApiResponse<List<VendorCategory>> getAllVendorCategories() {
        return userService.getVendorCategories();
    }

    public ApiResponse<List<VendorResponse>> getAllVendors() {
        return userService.getAllVendors();
    }

    public ApiResponse<List<VendorResponse>> getVendorsByCategory(VendorCategory category) {
        return userService.getVendorsByCategory(category);
    }
}
