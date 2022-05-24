package org.lviv.crime.repository.event.type.reference;

import org.lviv.crime.entity.event.type.reference.EventTypeReferenceEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface EventTypeReferenceRepository {
    Mono<Void> create(Long eventId, List<Long> eventTypeIds);
    Flux<EventTypeReferenceEntity> findAll();
    Flux<Long> findEventTypeIdsByEventId(Long eventId);
    Mono<Void> remove(Long eventId);
    Mono<Void> update(Long eventId, List<Long> eventTypeIds);
}
