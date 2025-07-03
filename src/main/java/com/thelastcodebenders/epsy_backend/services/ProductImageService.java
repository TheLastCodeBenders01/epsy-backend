package com.thelastcodebenders.epsy_backend.services;

import com.thelastcodebenders.epsy_backend.models.entities.ProductImage;
import com.thelastcodebenders.epsy_backend.repositories.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductImageService {
    private final ProductImageRepository productImageRepository;

    public List<ProductImage> findAllByProductId(Long productId) {
        return productImageRepository.findAllByProductId(productId);
    }

    public List<ProductImage> saveProductImages(Long productId, List<String> productImageUrls) {
        return productImageRepository.saveAll(
                productImageUrls.stream().map(
                        product -> ProductImage.builder()
                                .productId(productId)
                                .imageUrl(product)
                                .build()
                ).toList()
        );
    }
}
