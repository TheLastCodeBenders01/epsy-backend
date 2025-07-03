package com.thelastcodebenders.epsy_backend.models.dto;

import com.thelastcodebenders.epsy_backend.models.types.VendorCategory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    private String location;
    
    private Boolean isVendor;

    @NotNull(message = "vendor categories must not be null")
    @Size(min = 1, message = "Vendor categories list must contain at least one category")
    private List<VendorCategory> vendorCategories;

    private String imageUrl;
    private String displayName;
    private String telegramUsername;
}