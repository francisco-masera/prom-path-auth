package org.dargor.auth.util;

import org.dargor.auth.dto.SignUpDto;
import org.dargor.auth.dto.UserResponseDto;
import org.dargor.auth.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", expression = "java(true)")
    Customer signUpDtoToCustomer(SignUpDto signUpDto);

    UserResponseDto customerToUserResponse(Customer customer, String token);

}
