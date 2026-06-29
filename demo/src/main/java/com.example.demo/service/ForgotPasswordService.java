package com.example.demo.service;

public interface ForgotPasswordService {
    String sendOtp(String email);
    String verifyOtp(String email, String otp);
    String resetPassword(String email, String newPassword);
}
