package com.thelastcodebenders.epsy_backend.controllers;

import com.thelastcodebenders.epsy_backend.models.dto.ApiResponse;
import com.thelastcodebenders.epsy_backend.models.dto.AuthRequest;
import com.thelastcodebenders.epsy_backend.models.dto.AuthResponse;
import com.thelastcodebenders.epsy_backend.models.dto.ForgotPasswordRequest;
import com.thelastcodebenders.epsy_backend.models.dto.GenerateOtpRequest;
import com.thelastcodebenders.epsy_backend.models.dto.RegisterRequest;
import com.thelastcodebenders.epsy_backend.models.dto.ResetPasswordRequest;
import com.thelastcodebenders.epsy_backend.models.dto.VerifyOtpRequest;
import com.thelastcodebenders.epsy_backend.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Boolean>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request
    ) {
        return ResponseEntity.ok(authenticationService.verifyEmailOtp(request.getEmail(), request.getCode()));
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<ApiResponse<Void>> generateOtp(
            @Valid @RequestBody GenerateOtpRequest request
    ) {
        return ResponseEntity.ok(authenticationService.generateEmailVerificationOtp(request.getEmail()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        return ResponseEntity.ok(authenticationService.forgotPassword(request.getEmail()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Boolean>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        return ResponseEntity.ok(authenticationService.resetPassword(
                request.getEmail(),
                request.getCode(),
                request.getNewPassword()
        ));
    }
}
