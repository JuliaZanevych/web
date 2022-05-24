package org.lviv.crime.service.user;

import org.lviv.crime.dto.user.UserCreateDto;
import org.lviv.crime.dto.user.UserDto;
import org.lviv.crime.dto.user.UserItemDto;
import org.lviv.crime.dto.user.UserUpdateDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<UserDto> create(UserCreateDto userContentDto);

    Flux<UserItemDto> findAllItems();

    Mono<UserDto> getCurrentUser();

    Mono<UserDto> findById(Long userId);

    Mono<Void> update(Long userId, UserUpdateDto userUpdateDto);

    Mono<Void> remove(Long userId);
    
}
