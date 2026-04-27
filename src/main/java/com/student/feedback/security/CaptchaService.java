package com.student.feedback.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CaptchaService {

    @Value("${RECAPTCHA_SECRET:6Ldzaa0sAAAAABLhmRFfIaH8FA6pnb1NFj4tFvmS}")
    private String secretKey;

    public boolean verifyCaptcha(String token) {

        String url = "https://www.google.com/recaptcha/api/siteverify";

        RestTemplate restTemplate = new RestTemplate();

        Map response = restTemplate.postForObject(
                url + "?secret=" + secretKey + "&response=" + token,
                null,
                Map.class
        );

        return (Boolean) response.get("success");
    }
}
