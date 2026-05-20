package com.example.demo.service.impl;

import com.example.demo.domain.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.BusinessValidationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.records.PartnerDistance;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Override
    public UserDto saveUser(UserDto userDto) {

        if (userRepository.existsByPhoneNumber(userDto.getPhoneNumber())) {

            throw new BusinessValidationException("Phone number already exists, please use a different phone number");
        }

        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto getById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + id ));

        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getByFirstname(String firstName) {

        List<User> users = userRepository.findByFirstName(firstName);

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("User not found with first name: " + firstName );
        }

        return userMapper.toDtoList(users);
    }

    @Override
    public UserDto updateUser(Long id, UserDto updatedUser) {

        User existing = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + id ));

        // optional: check duplicate phone (excluding current user)
        if (userRepository.existsByPhoneNumber(updatedUser.getPhoneNumber())
                && !existing.getPhoneNumber().equals(updatedUser.getPhoneNumber())) {

            throw new BusinessValidationException("Phone number already exists" );
        }

        // update fields via MapStruct
        userMapper.updateFromDto(updatedUser, existing);

        return userMapper.toDto(userRepository.save(existing));
    }
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return;
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> searchUsers(String firstName, String lastName, String phoneNumber) {

        if (firstName == null && lastName == null && phoneNumber == null) {
            throw new BadRequestException("At least one search parameter must be provided");
        }

        List<User> users = userRepository.searchUsers(firstName, lastName, phoneNumber);

        if (users.isEmpty()) {

            throw new ResourceNotFoundException("No matching users found");
        }

        return userMapper.toDtoList(users);
    }
    @Override
    public List<PartnerDistance> getNearbyPartners(double latitude,double longitude, double radius) {

        List<Object[]> results =
                userRepository.findNearbyPartners(latitude,longitude,radius);

        return results.stream()
                .map(result -> new PartnerDistance(
                        ((Number) result[0]).longValue(),
                        (String) result[1],
                        (String) result[2],
                        Math.round(((Number) result[3]).doubleValue() * 10.0) / 10.0,
                        radius))
                .toList();
    }
    }
