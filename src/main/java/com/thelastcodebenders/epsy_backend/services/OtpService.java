package com.thelastcodebenders.epsy_backend.services;

import com.thelastcodebenders.epsy_backend.models.entities.Otp;
import com.thelastcodebenders.epsy_backend.models.types.OtpPurpose;
import com.thelastcodebenders.epsy_backend.repositories.OtpRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final JavaMailSender mailSender;
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 10;

    /**
     * Generates a new OTP for the given email and purpose.
     * If an unused OTP already exists for the same email and purpose, it is invalidated.
     *
     * @param email   The email to generate the OTP for
     * @param purpose The purpose of the OTP (EMAIL_VERIFICATION or PASSWORD_RESET)
     */
    public void generateOtp(String email, OtpPurpose purpose) {
        // Invalidate any existing OTPs for this email and purpose
        Optional<Otp> existingOtp = otpRepository.findTopByEmailAndPurposeOrderByCreatedAtDesc(email, purpose);
        existingOtp.ifPresent(otp -> {
            otp.setUsed(true);
            otpRepository.save(otp);
        });

        // Generate a new OTP
        String code = generateRandomCode(OTP_LENGTH);
        Instant expiryDate = Instant.now().plus(OTP_EXPIRY_MINUTES, ChronoUnit.MINUTES);

        Otp otp = Otp.builder()
                .email(email)
                .code(code)
                .expiryDate(expiryDate)
                .purpose(purpose)
                .build();

        otpRepository.save(otp);

        sendOtpEmail(email, code, purpose);

        log.info("OTP generated for email: {}, purpose: {}, code: {}", email, purpose, code);
    }

    /**
     * Verifies an OTP for the given email, code, and purpose.
     * If the OTP is valid, it is marked as used.
     *
     * @param email   The email associated with the OTP
     * @param code    The OTP code to verify
     * @param purpose The purpose of the OTP
     * @return true if the OTP is valid, false otherwise
     */
    public boolean verifyOtp(String email, String code, OtpPurpose purpose) {
        Optional<Otp> otpOptional = otpRepository.findByEmailAndCodeAndPurposeAndUsedFalse(email, code, purpose);

        if (otpOptional.isEmpty()) {
            return false;
        }

        Otp otp = otpOptional.get();

        if (otp.isExpired()) {
            return false;
        }

        // Mark the OTP as used
        otp.setUsed(true);
        otpRepository.save(otp);

        return true;
    }

    /**
     * Generates a random numeric code of the specified length.
     *
     * @param length The length of the code to generate
     * @return The generated code
     */
    private String generateRandomCode(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private void sendOtpEmail(String to, String code, OtpPurpose purpose) {
        String subject = "Your OTP Code for " + purpose.name().replace('_', ' ');
        String text = String.format(
            "Your OTP code for %s is: %s\n\nThis code will expire in %d minutes.",
            purpose.name().replace('_', ' ').toLowerCase(),
            code,
            OTP_EXPIRY_MINUTES
        );

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, false);
            mailSender.send(message);
            log.info("OTP email sent to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send OTP email to {}: {}", to, e.getMessage());
        }
    }
}