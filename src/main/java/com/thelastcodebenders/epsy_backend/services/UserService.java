package com.thelastcodebenders.epsy_backend.services;

import com.thelastcodebenders.epsy_backend.exceptions.UserNotFoundException;
import com.thelastcodebenders.epsy_backend.models.dto.ApiResponse;
import com.thelastcodebenders.epsy_backend.models.dto.VendorResponse;
import com.thelastcodebenders.epsy_backend.models.entities.User;
import com.thelastcodebenders.epsy_backend.models.types.VendorCategory;
import com.thelastcodebenders.epsy_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public ApiResponse<List<VendorCategory>> getVendorCategories() {
        return ApiResponse.success("successfully got categories", Arrays.stream(VendorCategory.values()).toList());
    }

    @SneakyThrows
    public ApiResponse<List<VendorResponse>> getAllVendors() {
        List<User> user = userRepository.findAllByIsVendorTrueAndEmailVerifiedTrue();
        return ApiResponse.success("successfully got vendors",
                user.parallelStream().map(User::toVendorResponse).toList());
    }

    public ApiResponse<List<VendorResponse>> getVendorsByCategory(VendorCategory category) {
        List<User> user = userRepository.findAllByIsVendorTrueAndVendorCategoriesContainingIgnoreCaseAndEmailVerifiedTrue(category.name());
        return ApiResponse.success("successfully got vendors",
                user.parallelStream().map(User::toVendorResponse).toList());
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}
