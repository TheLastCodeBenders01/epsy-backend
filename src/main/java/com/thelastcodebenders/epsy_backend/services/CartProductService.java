package com.thelastcodebenders.epsy_backend.services;

import com.thelastcodebenders.epsy_backend.models.entities.CartProduct;
import com.thelastcodebenders.epsy_backend.models.entities.Product;
import com.thelastcodebenders.epsy_backend.models.entities.User;
import com.thelastcodebenders.epsy_backend.repositories.CartProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartProductService {
    private final CartProductRepository cartProductRepository;

    public void addCartProduct(UUID cartId, Product product, int quantity, User vendor) {
        CartProduct cartProduct = cartProductRepository.findByCartIdAndProductId(cartId, product.getProductId()).orElse(
                CartProduct.builder()
                        .cartId(cartId)
                        .productName(product.getName())
                        .vendorName(vendor.getDisplayName())
                        .productId(product.getProductId())
                        .pricePerUnit(product.getPrice())
                        .build()
        );
        cartProduct.setQuantity(cartProduct.getQuantity() + quantity);

        saveCartProduct(cartProduct);
        log.info("Saved Cart product: {}", cartProduct);
    }

    public void saveCartProduct(CartProduct cartProduct) {
        cartProductRepository.save(cartProduct);
    }

    public List<CartProduct> getCartProductsForCart(UUID userId) {
        return cartProductRepository.findByCartId(userId);
    }
}
