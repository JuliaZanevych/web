package org.lviv.crime.mapper.event;

import org.lviv.crime.dto.event.EventContentDto;
import org.lviv.crime.dto.event.EventDto;
import org.lviv.crime.dto.event.EventItemDto;
import org.lviv.crime.entity.event.EventEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventEntity toEntity(EventContentDto eventContentDto);

    EventDto toDto(EventEntity eventEntity);

    EventItemDto toItemDto(EventEntity eventEntity);

    List<EventItemDto> toDtoList(List<EventEntity> eventEntities);
}
