package org.lviv.crime.mapper.user;

import org.lviv.crime.dto.user.UserCreateDto;
import org.lviv.crime.dto.user.UserDto;
import org.lviv.crime.dto.user.UserItemDto;
import org.lviv.crime.dto.user.UserUpdateDto;
import org.lviv.crime.entity.user.UserEntity;
import org.lviv.crime.entity.user.UserUpdateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(UserCreateDto dto);

    UserUpdateEntity toEntity(UserUpdateDto dto);

    UserDto toDto(UserEntity entity);

    UserItemDto toItemDto(UserEntity entity);

}
