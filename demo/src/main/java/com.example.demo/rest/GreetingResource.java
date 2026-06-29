package com.example.demo.rest;

import com.example.demo.domain.User;
import com.example.demo.service.GreetingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/call")
public class GreetingResource {
    // DI
    private final GreetingService greetingService;

    public GreetingResource(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/greeting")
    public ResponseEntity<String> getGreeting() {
        String message = greetingService.getGreeting();
        return ResponseEntity.ok(message);
    }

    @GetMapping("/content")
    public ResponseEntity<String> getContent() {
        String message = greetingService.getContent();
        return ResponseEntity.ok(message);
    }

//    @GetMapping("/user")
//    public ResponseEntity<User> getUser() {
//        User user = greetingService.getUser();
//        return ResponseEntity.ok(user);
//    }
//
//    @GetMapping("/user1")
//    public ResponseEntity<User> getUser1() {
//        User user1 = greetingService.getUser1();
//        return ResponseEntity.ok(user1);
//    }


    }

