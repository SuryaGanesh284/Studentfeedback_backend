package com.student.feedback.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.student.feedback.dto.LoginRequest;
import com.student.feedback.dto.RegisterRequest;
import com.student.feedback.dto.OtpVerifyRequest;
import com.student.feedback.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    // ================= VERIFY OTP =================
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyRequest request) {
        String response = authService.verifyOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(response);
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        String response = authService.login(request);

        // 🔥 If login failed
        if (response.equals("User not found") ||
            response.equals("Invalid password") ||
            response.equals("Please verify your email first")) {

            return ResponseEntity.status(401).body(response);
        }

        // 🔥 If success → JWT token
        return ResponseEntity.ok(response);
    }

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody Map<String, String> request) {

        String email = request.get("email");

        return authService.forgotPassword(email);
    }


    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody Map<String, String> request) {

        return authService.resetPassword(
            request.get("email"),
            request.get("otp"),
            request.get("newPassword")
        );
    }

}
