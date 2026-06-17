package com.example.demo.rest;
import com.example.demo.config.ApplicationProperties;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ApplicationPropertiesResource {

    private ApplicationProperties applicationProperties;

    @GetMapping("/otp")
    public ResponseEntity<Map<String, Object>> getOtpConfig() {

        ApplicationProperties.Otp otp = applicationProperties.getOtp();

        Map<String, Object> response = Map.of(
                "fallbackEnabled", otp.getFallbackEnabled(),
                "fallbackValue", otp.getFallbackValue()
        );

        return ResponseEntity.ok(response);
    }
}
