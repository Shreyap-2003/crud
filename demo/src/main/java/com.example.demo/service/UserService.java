package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.records.PartnerDistance;


import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto saveUser(UserDto userDto);


    UserDto getById(Long id);

    List<UserDto> getByFirstname(String firstName);

    UserDto updateUser(Long id, UserDto updatedUser);

    void deleteUser(Long id);


    List<UserDto> searchUsers(String name, String middleName, String phoneNumber);

    List<PartnerDistance> getNearbyPartners(double latitude, double longitude, double radius);

}