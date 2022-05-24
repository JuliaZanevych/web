package org.lviv.crime.service.user.impl;

import lombok.AllArgsConstructor;
import org.lviv.crime.dto.auth.User;
import org.lviv.crime.dto.user.UserCreateDto;
import org.lviv.crime.dto.user.UserDto;
import org.lviv.crime.dto.user.UserItemDto;
import org.lviv.crime.dto.user.UserUpdateDto;
import org.lviv.crime.entity.user.UserEntity;
import org.lviv.crime.entity.user.UserUpdateEntity;
import org.lviv.crime.mapper.user.UserMapper;
import org.lviv.crime.repository.user.UserRepository;
import org.lviv.crime.service.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.lviv.crime.constants.Constants.USER_CTX_KEY;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDto> create(UserCreateDto userDto) {
        UserEntity userEntity = userMapper.toEntity(userDto);
        userEntity.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.create(userEntity).map(userMapper::toDto);
    }

    @Override
    public Flux<UserItemDto> findAllItems() {
        return userRepository.findAllItems().map(userMapper::toItemDto);
    }

    @Override
    public Mono<UserDto> findById(Long userId) {
        return userRepository.findById(userId).map(userMapper::toDto);
    }

    @Override
    public Mono<UserDto> getCurrentUser() {
        return Mono.subscriberContext().flatMap(ctx -> {
            User user = ctx.get(USER_CTX_KEY);
            return userRepository.findById(user.getId()).map(userMapper::toDto);
        });
    }

    @Override
    public Mono<Void> update(Long userId, UserUpdateDto userDto) {
        UserUpdateEntity userEntity = userMapper.toEntity(userDto);
        userEntity.setId(userId);

        return userRepository.update(userEntity);
    }

    @Override
    public Mono<Void> remove(Long userId) {
       return userRepository.remove(userId);
    }
}
