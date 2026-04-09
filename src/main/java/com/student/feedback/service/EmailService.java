package com.student.feedback.service;

public interface EmailService {

    void sendEmail(String to, String subject, String text);
}