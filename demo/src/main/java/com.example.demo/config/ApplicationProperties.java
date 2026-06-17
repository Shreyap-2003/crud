package com.example.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private Otp otp = new Otp();

    @Getter
    @Setter
    public static class Otp {
        private Boolean fallbackEnabled;
        private String fallbackValue;
    }
}