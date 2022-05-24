package org.lviv.crime.repository.user.impl;

import lombok.AllArgsConstructor;
import org.lviv.crime.entity.user.UserColumn;
import org.lviv.crime.entity.user.UserEntity;
import org.lviv.crime.entity.user.UserUpdateEntity;
import org.lviv.crime.exception.NotFoundException;
import org.lviv.crime.repository.user.UserRepository;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.r2dbc.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.empty;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final NotFoundException NOT_FOUND_EXCEPTION = new NotFoundException("User not found!");

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Mono<UserEntity> create(UserEntity userEntity) {
        return r2dbcEntityTemplate.insert(userEntity);
    }

    @Override
    public Flux<UserEntity> findAllItems() {
        return r2dbcEntityTemplate.select(UserEntity.class)
                .matching(empty().columns(
                        UserColumn.id.name(),
                        UserColumn.username.name(),
                        UserColumn.firstName.name(),
                        UserColumn.middleName.name(),
                        UserColumn.lastName.name(),
                        UserColumn.email.name(),
                        UserColumn.phone.name()
                )).all();
    }

    @Override
    public Mono<UserEntity> findById(Long userId) {
        return r2dbcEntityTemplate.select(UserEntity.class)
                .matching(query(
                        where(UserColumn.id.name()).is(userId)
                ).columns(
                        UserColumn.ALL_COLUMN_NAMES
                )).first()
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    @Override
    public Mono<UserEntity> findByUsername(String username) {
        return r2dbcEntityTemplate.select(UserEntity.class)
                .matching(query(
                        where(UserColumn.username.name()).is(username)
                ).columns(
                        UserColumn.id.name(),
                        UserColumn.passwordHash.name()
                )).first();
    }

    @Override
    public Mono<Void> update(UserUpdateEntity userEntity) {
        return r2dbcEntityTemplate.update(userEntity)
                .onErrorResume(TransientDataAccessResourceException.class, e -> Mono.error(NOT_FOUND_EXCEPTION))
                .then();
    }

    @Override
    public Mono<Void> remove(Long userId) {
        return r2dbcEntityTemplate.delete(UserEntity.class)
                .matching(query(
                        where(UserColumn.id.name()).is(userId)
                )).all()
                .doOnSuccess(affectedRows -> {
                    if (affectedRows == 0) {
                        throw NOT_FOUND_EXCEPTION;
                    }
                }).then();
    }
}
