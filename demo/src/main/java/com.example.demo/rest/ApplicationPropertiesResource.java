package com.example.demo.rest;
import com.example.demo.config.ApplicationProperties;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    // PUT - update properties
    @PutMapping("/otp")
    public ResponseEntity<Map<String, Object>> updateOtpConfig(
            @RequestBody Map<String, Object> request) {

        ApplicationProperties.Otp otp = applicationProperties.getOtp();

        if (request.containsKey("fallbackEnabled")) {
            otp.setFallbackEnabled(
                    (Boolean) request.get("fallbackEnabled"));
        }

        if (request.containsKey("fallbackValue")) {
            otp.setFallbackValue(
                    (String) request.get("fallbackValue"));
        }

        return ResponseEntity.ok(Map.of(
                "message", "Properties updated successfully",
                "fallbackEnabled", otp.getFallbackEnabled(),
                "fallbackValue", otp.getFallbackValue()));
    }
}

