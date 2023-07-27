package org.dargor.auth.util;

import org.dargor.auth.dto.TokenResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TokenMapper {

    TokenMapper INSTANCE = Mappers.getMapper(TokenMapper.class);

    @Mapping(target = "token", source = ".")
    TokenResponseDto signUpDtoToUser(String token);

}
