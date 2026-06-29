package com.example.demo.rest;

import com.example.demo.domain.User;
import com.example.demo.dto.AuthRequest;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/application/auth")
@AllArgsConstructor
public class AuthResource {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthRequest authRequest,HttpServletRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    authRequest.getPhoneNumber(),authRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            HttpSession session = request.getSession(true);

            User user = userRepository.findByPhoneNumber(authRequest.getPhoneNumber())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return ResponseEntity.ok()
                    .header("X-Auth-Token", session.getId())
                    .body(
                            Map.of(
                                    "status", "SUCCESS",
                                    "message", "LOGIN_SUCCESSFUL",
                                    "name", user.getFirstName(),
                                    "customerId", user.getId(),
                                    "userType", user.getUserType()
                            ));

        } catch (Exception e) {

            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of("status", "FAILED",
                                    "message", "INVALID_PHONE_NUMBER_OR_PASSWORD"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader(value = "X-Auth-Token", required = false) String token,
            HttpServletRequest request) {

        if (token == null || token.isBlank()) {

            return ResponseEntity.status(401)
                    .body(Map.of(
                            "status", "FAILED",
                            "message", "INVALID_TOKEN"));
        }

        HttpSession session = request.getSession(false);

        if (session == null) {

            return ResponseEntity.status(401)
                    .body(Map.of(
                            "status", "FAILED",
                            "message", "USER_ALREADY_LOGGED_OUT"));
        }

        if (!session.getId().equals(token)) {

            return ResponseEntity.status(401)
                    .body(Map.of(
                            "status", "FAILED",
                            "message", "USER_ALREADY_LOGGED_OUT_OR_INVALID_TOKEN"));
        }

        session.invalidate();

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(
                Map.of("status", "SUCCESS",
                        "message", "LOGGED_OUT"));
    }
    }


