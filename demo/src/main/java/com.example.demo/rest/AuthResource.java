package com.example.demo.rest;

import com.example.demo.dto.AuthRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/application/auth")
@AllArgsConstructor
public class AuthResource {

    private final AuthenticationManager authenticationManager;
    private final HttpSessionSecurityContextRepository securityContextRepository
            = new HttpSessionSecurityContextRepository();

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthRequest authRequest,HttpServletRequest request,
            HttpServletResponse response) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    authRequest.getPhoneNumber(),authRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            HttpSession session = request.getSession(true);

            return ResponseEntity.ok()
                    .header("X-Auth-Token", session.getId())
                    .body(
                            Map.of(
                                    "status", "SUCCESS",
                                    "message", "LOGIN_SUCCESSFUL"));

        } catch (Exception e) {

            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "status", "FAILED",
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
                Map.of(
                        "status", "SUCCESS",
                        "message", "LOGGED_OUT"));
    }
    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(
//            HttpServletRequest request
//    ) {
//
//        request.getSession().invalidate();
//
//        return ResponseEntity.ok("Logout successful");
//    }
