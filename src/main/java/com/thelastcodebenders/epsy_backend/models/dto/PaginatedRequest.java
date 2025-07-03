package com.thelastcodebenders.epsy_backend.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaginatedRequest {
    @NotBlank
    private int pageNumber;
    @NotBlank
    private int pageSize;
}
