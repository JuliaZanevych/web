package org.lviv.crime.repository.event;

import org.lviv.crime.entity.event.EventEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventRepository {

    Mono<EventEntity> create(EventEntity eventEntity);

    Flux<EventEntity> findAll();

    Mono<EventEntity> findById(Long eventId);

    Mono<Void> update(EventEntity eventEntity);

    Mono<Void> remove(Long eventId);
}
