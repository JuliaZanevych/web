package org.lviv.crime.repository.user;

import org.lviv.crime.entity.user.UserEntity;
import org.lviv.crime.entity.user.UserUpdateEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<UserEntity> create(UserEntity UserEntity);

    Flux<UserEntity> findAllItems();

    Mono<UserEntity> findByUsername(String username);

    Mono<UserEntity> findById(Long UserId);

    Mono<Void> update(UserUpdateEntity UserEntity);

    Mono<Void> remove(Long UserId);
}