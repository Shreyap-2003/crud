package com.example.demo.mapper;

import com.example.demo.domain.User;
import com.example.demo.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDto, User> {

    UserDto toDto(User user);

    List<UserDto> toDtoList(List<User> users);

    User toEntity(UserDto userDTO);

    void updateFromDto(UserDto dto, @MappingTarget User entity);
}