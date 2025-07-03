package com.thelastcodebenders.epsy_backend.services;

import com.thelastcodebenders.epsy_backend.models.dto.ApiResponse;
import com.thelastcodebenders.epsy_backend.models.dto.CartResponse;
import com.thelastcodebenders.epsy_backend.models.entities.Cart;
import com.thelastcodebenders.epsy_backend.models.entities.CartProduct;
import com.thelastcodebenders.epsy_backend.models.entities.Product;
import com.thelastcodebenders.epsy_backend.models.entities.User;
import com.thelastcodebenders.epsy_backend.repositories.CartRepository;
import com.thelastcodebenders.epsy_backend.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartProductService cartProductService;
    private final ProductService productService;
    private final UserService userService;

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

    public ApiResponse<CartResponse> addProductToCart(Long productId, Integer quantity) {
        User user = UserUtil.getLoggedInUser();

        // validate user cart
        Cart cart = cartRepository.findById(user.getUserId()).get();
        Product product = productService.getProductById(productId);
        User vendor = userService.getUserById(product.getOwnerId());


        // build cart product
        cartProductService.addCartProduct(cart.getUserId(), product, quantity, vendor);

        return ApiResponse.success("product added to cart", buildCartResponse(cart));
    }

    private CartResponse buildCartResponse(Cart cart) {
        List<CartProduct> cartProducts = cartProductService.getCartProductsForCart(cart.getUserId());
        return CartResponse.builder()
                .cartId(cart.getUserId())
                .totalAmount(cartProducts.stream().mapToDouble(
                        cartProduct -> cartProduct.getPricePerUnit() * cartProduct.getQuantity()
                ).sum())
                .products(cartProducts)
                .build();
    }

    public ApiResponse<CartResponse> getUserCart() {
        return ApiResponse.success("success",
                buildCartResponse(cartRepository.findById(UserUtil.getLoggedInUser().getUserId()).get()));
    }
}
