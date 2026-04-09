package com.student.feedback.util;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class OtpGenerator {

    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}