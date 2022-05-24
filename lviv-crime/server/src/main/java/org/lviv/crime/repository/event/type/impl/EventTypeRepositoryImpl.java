package org.lviv.crime.repository.event.type.impl;

import lombok.AllArgsConstructor;
import org.lviv.crime.entity.event.type.EventTypeColumn;
import org.lviv.crime.entity.event.type.EventTypeEntity;
import org.lviv.crime.exception.NotFoundException;
import org.lviv.crime.repository.event.type.EventTypeRepository;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.relational.core.query.Query.empty;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@AllArgsConstructor
public class EventTypeRepositoryImpl implements EventTypeRepository {

    private final NotFoundException NOT_FOUND_EXCEPTION = new NotFoundException("Event Type not found!");

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Mono<EventTypeEntity> create(EventTypeEntity eventTypeEntity) {
        return r2dbcEntityTemplate.insert(eventTypeEntity);
    }

    @Override
    public Flux<EventTypeEntity> findAll() {
        return r2dbcEntityTemplate.select(EventTypeEntity.class)
                .matching(empty().columns(
                        EventTypeColumn.ALL_COLUMN_NAMES
                ).sort(by(asc(EventTypeColumn.name.name())))).all();
    }

    @Override
    public Mono<EventTypeEntity> findById(Long eventTypeId) {
        return r2dbcEntityTemplate.select(EventTypeEntity.class)
                .matching(query(
                        Criteria.where(EventTypeColumn.id.name()).is(eventTypeId)
                ).columns(
                        EventTypeColumn.ALL_COLUMN_NAMES
                )).first()
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    @Override
    public Mono<Void> update(EventTypeEntity eventTypeEntity) {
        return r2dbcEntityTemplate.update(eventTypeEntity)
                .onErrorResume(TransientDataAccessResourceException.class, e -> Mono.error(NOT_FOUND_EXCEPTION))
                .then();
    }

    @Override
    public Mono<Void> remove(Long eventTypeId) {
        return r2dbcEntityTemplate.delete(EventTypeEntity.class)
                .matching(query(
                        Criteria.where(EventTypeColumn.id.name()).is(eventTypeId)
                )).all()
                .doOnSuccess(affectedRows -> {
                    if (affectedRows == 0) {
                        throw NOT_FOUND_EXCEPTION;
                    }
                }).then();
    }
}
