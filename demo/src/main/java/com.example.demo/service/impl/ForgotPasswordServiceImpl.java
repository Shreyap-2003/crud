package com.example.demo.service.impl;


//import com.example.demo.config.FallbackProperties;
import com.example.demo.config.ApplicationProperties;
import com.example.demo.domain.Otp;
import com.example.demo.domain.User;
import com.example.demo.exception.BusinessValidationException;
import com.example.demo.repository.OtpRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.ForgotPasswordService;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
@AllArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private UserRepository userRepository;
    private OtpRepository otpRepository;
    private EmailService emailService;
    private ApplicationProperties applicationProperties;

    // Reads fallback OTP value from fallback.properties
    private PasswordEncoder passwordEncoder;
    private final Set<String> verifiedEmails = new HashSet<>();

    @Override
    @Transactional
    public String sendOtp(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BusinessValidationException(
                                "No account found with email: " + email));
        // Generate random OTP
        String otp = String.format("%06d", new Random().nextInt(1000000));
        // Remove old OTP
        otpRepository.deleteByEmail(email);

        Otp resetOtp = new Otp(email, otp,
                LocalDateTime.now().plusMinutes(5),
                LocalDateTime.now());

        otpRepository.save(resetOtp);
        // Send OTP through email

        emailService.sendOtpEmail(email, otp);
        return "OTP sent successfully to " + email;
    }
    @Override
    public String verifyOtp(String email, String otp) {

        // FALLBACK OTP FLOW
        if (applicationProperties.getOtp().getFallbackEnabled()) {
            String fallbackOtp = applicationProperties.getOtp().getFallbackValue();
            if (!fallbackOtp.equals(otp)) {
                throw new BusinessValidationException("Invalid OTP");
            }
        }

        // EMAIL OTP FLOW
        else {
            Otp savedOtp = otpRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new BusinessValidationException(
                                    "OTP not found. Please request a new one."));

            if (!savedOtp.getOtp().equals(otp)) {

                throw new BusinessValidationException("Invalid OTP");
            }
            if (LocalDateTime.now()
                    .isAfter(savedOtp.getExpiryTime())) {
                otpRepository.deleteByEmail(email);
                throw new BusinessValidationException(
                        "OTP has expired. Please request a new one.");
            }
        }

        verifiedEmails.add(email);

        return "OTP verified successfully";
    }

    @Override
    @Transactional
    public String resetPassword(String email, String newPassword) {
        if (!verifiedEmails.contains(email)) {

            throw new BusinessValidationException(
                    "OTP verification required before resetting password.");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BusinessValidationException(
                                "No account found with email: " + email));

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        // Delete OTP after password reset
        otpRepository.deleteByEmail(email);

        // Remove verification
        verifiedEmails.remove(email);

        return "Password reset successfully";
    }
}