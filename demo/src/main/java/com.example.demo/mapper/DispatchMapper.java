package com.example.demo.mapper;

import com.example.demo.domain.Dispatch;
import com.example.demo.dto.DispatchDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DispatchMapper {

    DispatchDto toDto(Dispatch dispatch);

    Dispatch toEntity(DispatchDto dispatchDto);

    List<DispatchDto> toDtoList(List<Dispatch> dispatches);
}