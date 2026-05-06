package com.example.demo.rest;

import com.example.demo.service.UserService;
import com.example.demo.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class UserResource {

    private final UserService userService;


    @PostMapping("/users")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) {
        UserDto saved = userService.saveUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/user/id/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/user/name/{firstname}")
    public ResponseEntity<List<UserDto>> getByFirstname(
            @PathVariable String firstname) {

        return ResponseEntity.ok(
                userService.getByFirstname(firstname)
        );
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserDto updatedUser) {

        return ResponseEntity.ok(
                userService.updateUser(id, updatedUser)
        );
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.getById(id) == null) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userService.deleteUser(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/search")
    public ResponseEntity<List<UserDto>> searchUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String phoneNumber) {

        return ResponseEntity.ok(
                userService.searchUsers(firstName, lastName, phoneNumber));
    }
}