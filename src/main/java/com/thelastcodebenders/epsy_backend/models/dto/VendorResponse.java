package com.thelastcodebenders.epsy_backend.models.dto;

import com.thelastcodebenders.epsy_backend.models.types.VendorCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class VendorResponse {
    private String displayName;
    private String imageUrl;
    private UUID vendorId;
    private String telegramUsername;
    private List<VendorCategory> vendorCategories;
}
