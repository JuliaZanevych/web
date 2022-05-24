package org.lviv.crime.mapper.event.type;

import org.lviv.crime.dto.event.type.EventTypeContentDto;
import org.lviv.crime.dto.event.type.EventTypeDto;
import org.lviv.crime.entity.event.type.EventTypeEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventTypeMapper {

    EventTypeEntity toEntity(EventTypeContentDto eventTypeContentDto);

    EventTypeDto toDto(EventTypeEntity eventTypeEntity);

    List<EventTypeDto> toDtoList(List<EventTypeEntity> eventTypeEntities);
}
