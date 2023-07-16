package org.dargor.auth.util;

import org.dargor.auth.dto.SignUpRequestDto;
import org.dargor.auth.dto.UserResponseDto;
import org.dargor.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", expression = "java(true)")
    User signUpDtoToUser(SignUpRequestDto signUpDto);

    UserResponseDto userToUserResponse(User user, String token);

}
