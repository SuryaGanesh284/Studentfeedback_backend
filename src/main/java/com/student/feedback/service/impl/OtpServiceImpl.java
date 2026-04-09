package com.student.feedback.service.impl;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.feedback.entity.OtpVerification;
import com.student.feedback.repository.OtpRepository;
import com.student.feedback.service.OtpService;

import org.springframework.transaction.annotation.Transactional;


@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private OtpRepository otpRepository;

    // ================= GENERATE OTP =================
    @Override
    @Transactional
    public String generateOtp(String email) {

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        // Delete old OTP
        otpRepository.deleteByEmail(email);

        // Create new OTP
        OtpVerification otpEntity = OtpVerification.builder()
                .email(email)
                .otp(otp)
                .expiryTime(LocalDateTime.now().plusMinutes(5)) // ✅ FIXED expiry
                .build();

        otpRepository.save(otpEntity);

        // 🔥 DEBUG PRINTS
        System.out.println("Generated OTP: " + otp);
        System.out.println("EMAIL CHECK: " + email);

        return otp;
    }

    // ================= VERIFY OTP =================
    @Override
    @Transactional
    public boolean verifyOtp(String email, String otp) {

        OtpVerification storedOtp =
                otpRepository.findTopByEmailOrderByIdDesc(email).orElse(null);

        // ❌ No OTP found
        if (storedOtp == null) {
            System.out.println("No OTP found for email: " + email);
            return false;
        }

        // 🔥 DEBUG PRINTS (VERY IMPORTANT)
        System.out.println("DB OTP: " + storedOtp.getOtp());
        System.out.println("Entered OTP: " + otp);
        System.out.println("EMAIL CHECK: " + email);
        System.out.println("Expiry Time: " + storedOtp.getExpiryTime());
        System.out.println("Current Time: " + LocalDateTime.now());

        // ❌ OTP mismatch
        if (!storedOtp.getOtp().equals(otp)) {
            System.out.println("OTP MISMATCH ❌");
            return false;
        }

        // ❌ Expired OTP
        if (storedOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            System.out.println("OTP EXPIRED ❌");
            return false;
        }

        // ✅ OTP Verified
        System.out.println("OTP VERIFIED ✅");

        // Delete after success
        otpRepository.deleteByEmail(email);

        return true;
    }
}
