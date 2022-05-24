package org.lviv.crime.service.event.type.impl;

import lombok.AllArgsConstructor;
import org.lviv.crime.dto.event.type.EventTypeContentDto;
import org.lviv.crime.dto.event.type.EventTypeDto;
import org.lviv.crime.entity.event.type.EventTypeEntity;
import org.lviv.crime.mapper.event.type.EventTypeMapper;
import org.lviv.crime.repository.event.type.EventTypeRepository;
import org.lviv.crime.service.event.type.EventTypeService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class EventTypeServiceImpl implements EventTypeService {

    private final EventTypeRepository eventTypeRepository;

    private final EventTypeMapper eventTypeMapper;

    @Override
    public Mono<EventTypeDto> create(EventTypeContentDto eventTypeContentDto) {
        EventTypeEntity eventTypeEntity = eventTypeMapper.toEntity(eventTypeContentDto);

        return eventTypeRepository.create(eventTypeEntity)
                .map(createdEventTypeEntity -> eventTypeMapper.toDto(createdEventTypeEntity));
    }

    @Override
    public Flux<EventTypeDto> findAll() {
        return eventTypeRepository.findAll().collectList()
                .map(eventTypeEntities -> eventTypeMapper.toDtoList(eventTypeEntities))
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<EventTypeDto> findById(Long eventTypeId) {
        return eventTypeRepository.findById(eventTypeId).map(eventTypeEntity -> {
            EventTypeDto eventTypeDto = eventTypeMapper.toDto(eventTypeEntity);
            return eventTypeDto;
        });
    }

    @Override
    public Mono<Void> update(Long eventTypeId, EventTypeContentDto eventTypeContentDto) {
        EventTypeEntity eventTypeEntity = eventTypeMapper.toEntity(eventTypeContentDto);
        eventTypeEntity.setId(eventTypeId);

        return eventTypeRepository.update(eventTypeEntity);
    }

    @Override
    public Mono<Void> remove(Long eventTypeId) {
        return eventTypeRepository.remove(eventTypeId);
    }
}
