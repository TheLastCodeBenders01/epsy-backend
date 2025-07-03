package com.thelastcodebenders.epsy_backend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thelastcodebenders.epsy_backend.models.dto.ApiResponse;
import com.thelastcodebenders.epsy_backend.models.dto.AuthRequest;
import com.thelastcodebenders.epsy_backend.models.dto.AuthResponse;
import com.thelastcodebenders.epsy_backend.models.dto.RegisterRequest;
import com.thelastcodebenders.epsy_backend.models.entities.Cart;
import com.thelastcodebenders.epsy_backend.models.entities.User;
import com.thelastcodebenders.epsy_backend.models.types.OtpPurpose;
import com.thelastcodebenders.epsy_backend.models.types.Role;
import com.thelastcodebenders.epsy_backend.repositories.UserRepository;
import com.thelastcodebenders.epsy_backend.security.JwtService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final ObjectMapper objectMapper;
    private final CartService cartService;

    @Transactional
    public ApiResponse<AuthResponse> register(RegisterRequest request) {
        try {
            // Check if email already exists
            if (userRepository.existsByEmail(request.getEmail())) {
                return ApiResponse.error("Email already exists");
            }

            var user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .location(request.getLocation())
                    .isVendor(request.getIsVendor())
                    .imageUrl(request.getImageUrl())
                    .emailVerified(false) // Set email as not verified
                    .role(request.getIsVendor() ? Role.VENDOR : Role.USER)
                    .build();

            if (request.getIsVendor()) {
                user.setDisplayName(request.getDisplayName());
                user.setTelegramUsername(request.getTelegramUsername());
                user.setVendorCategories(objectMapper.writeValueAsString(request.getVendorCategories()));
            }

            // build cart
            Cart cart = Cart.builder()
                    .userId(user.getUserId())
                    .build();

            cartService.saveCart(cart);

            userRepository.save(user);

            // Generate OTP for email verification
            otpService.generateOtp(user.getEmail(), OtpPurpose.EMAIL_VERIFICATION);

            // In a real application, you would send the OTP to the user's email here
            // For now, we'll just return a response without a token

            AuthResponse response = AuthResponse.builder()
                    .userId(user.getUserId().toString())
                    .build();

            return ApiResponse.success(
                "User registered successfully. Please verify your email with the OTP sent.",
                response
            );
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    public ApiResponse<AuthResponse> authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Check if email is verified
            if (!user.isEmailVerified()) {
                // Generate a new OTP for email verification
                otpService.generateOtp(user.getEmail(), OtpPurpose.EMAIL_VERIFICATION);

                // Return response without token
                AuthResponse response = AuthResponse.builder()
                        .userId(user.getUserId().toString())
                        .build();

                return ApiResponse.success(
                    "Email not verified. Please verify your email with the OTP sent.",
                    response
                );
            }

            var jwtToken = jwtService.generateToken(user);

            AuthResponse response = AuthResponse.builder()
                    .token(jwtToken)
                    .userId(user.getUserId().toString())
                    .build();

            return ApiResponse.success("Login successful", response);
        } catch (AuthenticationException e) {
            return ApiResponse.error("Invalid email or password");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * Verifies an OTP for email verification.
     * If the OTP is valid, the user's email is marked as verified.
     *
     * @param email The email associated with the OTP
     * @param code  The OTP code to verify
     * @return ApiResponse containing true if the OTP is valid, or an error message
     */
    public ApiResponse<Boolean> verifyEmailOtp(String email, String code) {
        try {
            boolean isValid = otpService.verifyOtp(email, code, OtpPurpose.EMAIL_VERIFICATION);

            if (isValid) {
                var user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));

                user.setEmailVerified(true);
                userRepository.save(user);

                return ApiResponse.success("OTP verified successfully", true);
            } else {
                return ApiResponse.error("Invalid OTP");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * Generates a new OTP for email verification.
     *
     * @param email The email to generate the OTP for
     * @return ApiResponse with success or error message
     */
    public ApiResponse<Void> generateEmailVerificationOtp(String email) {
        try {
            if (!userRepository.existsByEmail(email)) {
                return ApiResponse.error("User not found");
            }

            otpService.generateOtp(email, OtpPurpose.EMAIL_VERIFICATION);
            return ApiResponse.success("OTP generated and sent successfully");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * Initiates the forgot password process by generating an OTP.
     *
     * @param email The email to generate the OTP for
     * @return ApiResponse with success or error message
     */
    public ApiResponse<Void> forgotPassword(String email) {
        try {
            if (!userRepository.existsByEmail(email)) {
                return ApiResponse.error("User not found");
            }

            otpService.generateOtp(email, OtpPurpose.PASSWORD_RESET);
            return ApiResponse.success("Password reset OTP sent successfully");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * Resets a user's password using an OTP.
     *
     * @param email       The email associated with the OTP
     * @param code        The OTP code to verify
     * @param newPassword The new password to set
     * @return ApiResponse containing true if the password was reset successfully, or an error message
     */
    public ApiResponse<Boolean> resetPassword(String email, String code, String newPassword) {
        try {
            boolean isValid = otpService.verifyOtp(email, code, OtpPurpose.PASSWORD_RESET);

            if (isValid) {
                var user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));

                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);

                return ApiResponse.success("Password reset successfully", true);
            } else {
                return ApiResponse.error("Invalid OTP");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
