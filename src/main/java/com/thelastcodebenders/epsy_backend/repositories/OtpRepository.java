package com.thelastcodebenders.epsy_backend.repositories;

import com.thelastcodebenders.epsy_backend.models.entities.Otp;
import com.thelastcodebenders.epsy_backend.models.types.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpRepository extends JpaRepository<Otp, UUID> {
    Optional<Otp> findByEmailAndCodeAndPurposeAndUsedFalse(String email, String code, OtpPurpose purpose);
    Optional<Otp> findTopByEmailAndPurposeOrderByCreatedAtDesc(String email, OtpPurpose purpose);
}
