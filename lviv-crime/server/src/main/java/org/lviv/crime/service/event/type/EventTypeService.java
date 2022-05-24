package org.lviv.crime.service.event.type;

import org.lviv.crime.dto.event.type.EventTypeContentDto;
import org.lviv.crime.dto.event.type.EventTypeDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventTypeService {

    Mono<EventTypeDto> create(EventTypeContentDto eventTypeContentDto);

    Flux<EventTypeDto> findAll();

    Mono<EventTypeDto> findById(Long eventTypeId);

    Mono<Void> update(Long eventTypeId, EventTypeContentDto eventTypeContentDto);

    Mono<Void> remove(Long eventTypeId);
}
