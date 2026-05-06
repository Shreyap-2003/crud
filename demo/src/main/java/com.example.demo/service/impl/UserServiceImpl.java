package com.example.demo.service.impl;

import com.example.demo.domain.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Override
    public UserDto saveUser(UserDto userDto) {

        if (userRepository.existsByPhoneNumber(userDto.getPhoneNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Phone number already exists, please use a different phone number"
            );
        }

        User user = userMapper.toEntity(userDto);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto getById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User not found with id: " + id));

        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getByFirstname(String firstName) {

        List<User> users = userRepository.findByFirstName(firstName);

        if (users.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found with first name: " + firstName
            );
        }

        return userMapper.toDtoList(users);
    }

    @Override
    public UserDto updateUser(Long id, UserDto updatedUser) {

        User existing = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User not found with id: " + id));

        // optional: check duplicate phone (excluding current user)
        if (userRepository.existsByPhoneNumber(updatedUser.getPhoneNumber())
                && !existing.getPhoneNumber().equals(updatedUser.getPhoneNumber())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Phone number already exists"
            );
        }

        // update fields via MapStruct
        userMapper.updateFromDto(updatedUser, existing);

        return userMapper.toDto(userRepository.save(existing));
    }
    @Override
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public List<UserDto> searchUsers(String firstName, String lastName, String phoneNumber) {

        if (firstName == null && lastName == null && phoneNumber == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "At least one search parameter must be provided"
            );
        }

        List<User> users = userRepository.searchUsers(firstName, lastName, phoneNumber);

        if (users.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No matching users found"
            );
        }

        return userMapper.toDtoList(users);
    }
}