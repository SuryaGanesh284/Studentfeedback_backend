 package com.student.feedback.service.impl;

import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.student.feedback.dto.LoginRequest;
import com.student.feedback.dto.RegisterRequest;
import com.student.feedback.entity.User;
import com.student.feedback.repository.UserRepository;
import com.student.feedback.security.JwtUtil;
import com.student.feedback.service.AuthService;
import com.student.feedback.service.EmailService;
import com.student.feedback.service.OtpService;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 🔥 TEMP STORE (OTP FLOW)
    private Map<String, String> tempPasswordStore = new HashMap<>();


    // ================= ADMIN AUTO CREATE =================
    @PostConstruct
    public void createAdmin() {

        if (userRepository.findByEmail("admin@university.edu").isEmpty()) {

            User admin = User.builder()
                    .name("Admin")
                    .email("admin@university.edu")
                    .password(passwordEncoder.encode("admin1234"))
                    .role("ADMIN")
                    .enabled(true)
                    .build();

            userRepository.save(admin);

            System.out.println("✅ Admin created successfully");
        }
    }


    // ================= REGISTER =================
    @Override
    public String register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "User already exists";
        }

        tempPasswordStore.put(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );

        String otp = otpService.generateOtp(request.getEmail());

        emailService.sendEmail(
                request.getEmail(),
                "OTP Verification",
                "Your OTP is: " + otp
        );

        return "OTP sent successfully";
    }


    // ================= VERIFY OTP =================
    @Override
    public String verifyOtp(String email, String otp) {

        boolean isValid = otpService.verifyOtp(email, otp);

        if (!isValid) return "Invalid OTP";

        String password = tempPasswordStore.get(email);

        if (password == null) {
            return "Session expired. Please register again.";
        }

        User user = User.builder()
                .name("Student")
                .email(email)
                .password(password)
                .role("STUDENT")
                .enabled(true)
                .build();

        userRepository.save(user);

        tempPasswordStore.remove(email);

        return "User registered successfully";
    }


    // ================= LOGIN =================
    @Override
    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) return "User not found";

        if (!user.isEnabled()) {
            return "Please verify your email first";
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return "Invalid password";
        }

        // ✅ DEBUG LOG
        System.out.println("LOGIN SUCCESS for: " + user.getEmail());

        // ✅ SEND LOGIN EMAIL (SAFE)
        try {
            emailService.sendEmail(
                user.getEmail(),
                "Login Alert",
                "Hello " + user.getName() + ",\n\n" +
                "You have successfully logged into the Student Feedback System.\n\n" +
                "If this was not you, please reset your password immediately.\n\n" +
                "- Student Feedback System"
            );

            System.out.println("LOGIN EMAIL SENT ✅");

        } catch (Exception e) {
            System.out.println("LOGIN EMAIL FAILED ❌: " + e.getMessage());
        }

        // 🔥 Generate JWT
        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }


    // ================= FORGOT PASSWORD =================
    @Override
    public String forgotPassword(String email) {

        if (userRepository.findByEmail(email).isEmpty()) {
            return "User not found";
        }

        String otp = otpService.generateOtp(email);

        emailService.sendEmail(
                email,
                "Reset Password OTP",
                "Your OTP is: " + otp
        );

        return "OTP sent to email";
    }


    // ================= RESET PASSWORD =================
    @Override
    public String resetPassword(String email, String otp, String newPassword) {

        boolean isValid = otpService.verifyOtp(email, otp);

        if (!isValid) return "Invalid OTP";

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) return "User not found";

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        return "Password reset successful";
    }
}
