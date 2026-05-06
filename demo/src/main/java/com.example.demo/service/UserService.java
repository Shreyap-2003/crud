package com.example.demo.service;

import com.example.demo.dto.UserDto;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto saveUser(UserDto userDto);


    UserDto getById(Long id);

    List<UserDto> getByFirstname(String firstName);

    UserDto updateUser(Long id, UserDto updatedUser);

    boolean deleteUser(Long id);


    List<UserDto> searchUsers(String name, String middleName, String phoneNumber);

}