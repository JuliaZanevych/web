package org.lviv.crime.repository.event.impl;

import lombok.AllArgsConstructor;
import org.lviv.crime.entity.event.EventColumn;
import org.lviv.crime.entity.event.EventEntity;
import org.lviv.crime.exception.NotFoundException;
import org.lviv.crime.repository.event.EventRepository;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.domain.Sort.Order.desc;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.relational.core.query.Query.empty;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@AllArgsConstructor
public class EventRepositoryImpl implements EventRepository {
    private final NotFoundException NOT_FOUND_EXCEPTION = new NotFoundException("Event not found!");

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Mono<EventEntity> create(EventEntity eventEntity) {
        return r2dbcEntityTemplate.insert(eventEntity);
    }

    @Override
    public Flux<EventEntity> findAll() {
        return r2dbcEntityTemplate.select(EventEntity.class)
                .matching(empty().columns(
                        EventColumn.ALL_COLUMN_NAMES
                ).sort(by(desc(EventColumn.id.name())))).all();
    }

    @Override
    public Mono<EventEntity> findById(Long eventId) {
        return r2dbcEntityTemplate.select(EventEntity.class)
                .matching(query(
                        Criteria.where(EventColumn.id.name()).is(eventId)
                ).columns(
                        EventColumn.ALL_COLUMN_NAMES
                )).first()
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    @Override
    public Mono<Void> update(EventEntity eventEntity) {
        return r2dbcEntityTemplate.update(eventEntity)
                .onErrorResume(TransientDataAccessResourceException.class, e -> Mono.error(NOT_FOUND_EXCEPTION))
                .then();
    }

    @Override
    public Mono<Void> remove(Long eventId) {
        return r2dbcEntityTemplate.delete(EventEntity.class)
                .matching(query(
                        Criteria.where(EventColumn.id.name()).is(eventId)
                )).all()
                .doOnSuccess(affectedRows -> {
                    if (affectedRows == 0) {
                        throw NOT_FOUND_EXCEPTION;
                    }
                }).then();
    }
}
