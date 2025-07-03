package com.thelastcodebenders.epsy_backend.models.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedResponse<T> {
    private List<T> items;
    private int pageNumber;
    private int pageSize;
}
