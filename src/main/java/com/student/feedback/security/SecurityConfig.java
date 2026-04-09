package com.student.feedback.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> {})
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth

                // ✅ SWAGGER (VERY IMPORTANT)
                .requestMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**"
                ).permitAll()

                // ✅ PUBLIC APIs (LOGIN / REGISTER / OTP)
                .requestMatchers("/api/auth/**").permitAll()

                // ✅ TEMP (optional)
                .requestMatchers("/api/feedback/**").permitAll()

                // 👑 ADMIN
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // 📚 COURSE
                .requestMatchers("/api/course/**").authenticated()

                // 👨‍🏫 INSTRUCTOR
                .requestMatchers("/api/instructor/**").authenticated()

                // 🏢 SERVICES
                .requestMatchers("/api/service/**").authenticated()

                // 🔐 ALL OTHER REQUESTS
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter,
                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    // ✅ PASSWORD ENCODER
    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }

    // ✅ CORS CONFIG
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
