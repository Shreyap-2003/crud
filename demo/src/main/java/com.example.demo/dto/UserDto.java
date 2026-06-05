package com.example.demo.dto;

import com.example.demo.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private UserType userType;

    private String password;

    private Double latitude;

    private Double longitude;

    private String address;
}