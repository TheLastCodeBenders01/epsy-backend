package com.thelastcodebenders.epsy_backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thelastcodebenders.epsy_backend.exceptions.ProductNotFoundException;
import com.thelastcodebenders.epsy_backend.models.dto.ApiResponse;
import com.thelastcodebenders.epsy_backend.models.dto.PaginatedRequest;
import com.thelastcodebenders.epsy_backend.models.dto.PaginatedResponse;
import com.thelastcodebenders.epsy_backend.models.dto.ProductRequest;
import com.thelastcodebenders.epsy_backend.models.dto.ProductResponse;
import com.thelastcodebenders.epsy_backend.models.entities.Product;
import com.thelastcodebenders.epsy_backend.models.entities.User;
import com.thelastcodebenders.epsy_backend.repositories.ProductRepository;
import com.thelastcodebenders.epsy_backend.utils.UserUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final ProductImageService productImageService;

    private List<ProductResponse> convertProductListToResponse(List<Product> products) {
        return products.parallelStream().map(
                product -> product.toDto(productImageService.findAllByProductId(product.getProductId()))
        ).toList();
    }

    public ApiResponse<PaginatedResponse<ProductResponse>> getProducts(PaginatedRequest paginatedRequest) {
        List<ProductResponse> products = convertProductListToResponse(productRepository.findAll(PageRequest.of(paginatedRequest.getPageNumber(), paginatedRequest.getPageSize())).getContent());
        PaginatedResponse<ProductResponse> response = PaginatedResponse.<ProductResponse>builder()
                .pageNumber(paginatedRequest.getPageNumber())
                .pageSize(paginatedRequest.getPageSize())
                .items(products)
                .build();
        return ApiResponse.success("successfully got products", response);
    }

    @Transactional
    public ApiResponse<ProductResponse> createProduct(ProductRequest request) throws JsonProcessingException {
        User user = UserUtil.getLoggedInUser();

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .ownerId(user.getUserId())
                .quantity(request.getQuantity())
                .build();
        if (request.getCategories() != null && !request.getCategories().isEmpty()) {
            product.setCategories(objectMapper.writeValueAsString(request.getCategories()));
        }
        saveProduct(product);

        return ApiResponse.success("product created successfully",
                product.toDto(productImageService.saveProductImages(product.getProductId(), request.getImageUrls())));
    }

    private void saveProduct(Product product) {
        productRepository.save(product);
    }

    public ApiResponse<List<ProductResponse>> getAllVendorProducts(UUID vendorId) {
        return ApiResponse.success("got all vendor products", productRepository.findAllByOwnerId(vendorId).parallelStream().map(
                product -> product.toDto(productImageService.findAllByProductId(product.getProductId()))
        ).toList());
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    }
}
