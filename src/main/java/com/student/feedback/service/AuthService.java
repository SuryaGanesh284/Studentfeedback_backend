package com.student.feedback.service;

import com.student.feedback.dto.LoginRequest;
import com.student.feedback.dto.RegisterRequest;

public interface AuthService {

    String register(RegisterRequest request);

    String verifyOtp(String email, String otp);

    String login(LoginRequest request);

    // 🔥 ADD THESE
    String forgotPassword(String email);

    String resetPassword(String email, String otp, String newPassword);
}
