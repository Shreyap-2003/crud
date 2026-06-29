package com.example.demo.mapper;

import com.example.demo.domain.Order;
import com.example.demo.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDto, Order> {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "partner.id", target = "partnerId")
    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.name", target = "itemName")
    @Mapping(source = "item.imageUrl", target = "imageUrl")
    @Mapping(source = "item.price", target = "price")
    @Mapping(source = "createdTime", target = "createdTime")
    @Mapping(source = "id", target = "id")
    @Mapping(expression = "java(order.getCustomer().getFirstName() + \" \" + order.getCustomer().getLastName())", target = "customerName")
    @Mapping(source = "customer.phoneNumber", target = "customerPhone")
    @Mapping(source = "customer.address", target = "customerAddress")
    OrderDto toDto(Order order);

    List<OrderDto> toDtoList(List<Order> orders);


    Order toEntity(OrderDto orderDto);

    @Mapping(target = "id", ignore = true)
    void updateFromDto(OrderDto dto, @MappingTarget Order entity);
}