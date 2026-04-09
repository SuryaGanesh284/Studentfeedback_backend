package com.student.feedback.security;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CaptchaService {

    private final String SECRET_KEY = "6Ldzaa0sAAAAABLhmRFfIaH8FA6pnb1NFj4tFvmS";

    public boolean verifyCaptcha(String token) {

        String url = "https://www.google.com/recaptcha/api/siteverify";

        RestTemplate restTemplate = new RestTemplate();

        Map response = restTemplate.postForObject(
                url + "?secret=" + SECRET_KEY + "&response=" + token,
                null,
                Map.class
        );

        return (Boolean) response.get("success");
    }
}