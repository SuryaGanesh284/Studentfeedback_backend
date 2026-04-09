package com.student.feedback.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.feedback.entity.OtpVerification;

public interface OtpRepository extends JpaRepository<OtpVerification, Long> {

    // 🔥 Get latest OTP
    Optional<OtpVerification> findTopByEmailOrderByIdDesc(String email);

    // 🔥 Needed for delete/reset
    void deleteByEmail(String email);

    // 🔥 ADD THIS (IMPORTANT)
    Optional<OtpVerification> findByEmail(String email);
}
