package org.lviv.crime.repository.event.type;

import org.lviv.crime.entity.event.type.EventTypeEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventTypeRepository {

    Mono<EventTypeEntity> create(EventTypeEntity eventTypeEntity);

    Flux<EventTypeEntity> findAll();

    Mono<EventTypeEntity> findById(Long eventTypeId);

    Mono<Void> update(EventTypeEntity eventTypeEntity);

    Mono<Void> remove(Long eventTypeId);
}
