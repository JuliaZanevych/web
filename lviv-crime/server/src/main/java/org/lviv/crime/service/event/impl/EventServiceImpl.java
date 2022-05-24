package org.lviv.crime.service.event.impl;

import lombok.AllArgsConstructor;
import org.lviv.crime.dto.event.EventContentDto;
import org.lviv.crime.dto.event.EventDto;
import org.lviv.crime.dto.event.EventItemDto;
import org.lviv.crime.entity.event.EventEntity;
import org.lviv.crime.entity.event.type.reference.EventTypeReferenceEntity;
import org.lviv.crime.exception.NotFoundException;
import org.lviv.crime.mapper.event.EventMapper;
import org.lviv.crime.repository.event.EventRepository;
import org.lviv.crime.repository.event.type.reference.EventTypeReferenceRepository;
import org.lviv.crime.service.event.EventService;
import org.lviv.crime.service.file.storage.FileStorageService;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private static final String EVENT_IMAGE_FOLDER = "events";

    private static final NotFoundException EVENT_IMAGE_NOT_FOUND_EXCEPTION
            = new NotFoundException("Event image found!");

    private final FileStorageService fileStorageService;

    private final EventRepository eventRepository;

    private final EventTypeReferenceRepository eventTypeReferenceRepository;

    private final EventMapper eventMapper;

    @Override
    public Mono<EventDto> create(EventContentDto eventContentDto) {
        EventEntity eventEntity = eventMapper.toEntity(eventContentDto);

        return eventRepository.create(eventEntity)
                .flatMap(createdEventEntity -> eventTypeReferenceRepository.create(createdEventEntity.getId(),
                        eventContentDto.getEventTypeIds()).thenReturn(createdEventEntity))
                .flatMap(createdEventEntity -> findById(createdEventEntity.getId()));
    }

    @Override
    public Flux<EventItemDto> findAllItems() {
        Mono<List<EventEntity>> eventsMono = eventRepository.findAll().collectList();
        Mono<List<EventTypeReferenceEntity>> eventTypeReferencesMono = eventTypeReferenceRepository.findAll().collectList();

        return Mono.zip(eventsMono, eventTypeReferencesMono).map(tuple -> {
            List<EventEntity> eventEntities = tuple.getT1();
            List<EventTypeReferenceEntity> eventTypeReferences = tuple.getT2();

            List<EventItemDto> eventItems = eventMapper.toDtoList(eventEntities);
            Map<Long, List<EventTypeReferenceEntity>> eventTypeReferenceMap = eventTypeReferences.stream()
                    .collect(groupingBy(EventTypeReferenceEntity::getEventId));

            eventItems.forEach(eventItem -> {
                Long eventId = eventItem.getId();
                List<EventTypeReferenceEntity> targetEventTypeReferences = eventTypeReferenceMap.get(eventId);

                List<Long> eventTypeIds = CollectionUtils.isEmpty(targetEventTypeReferences) ? Collections.EMPTY_LIST :
                        targetEventTypeReferences.stream()
                                .map(EventTypeReferenceEntity::getEventTypeId).collect(Collectors.toList());

                eventItem.setEventTypeIds(eventTypeIds);
            });

            return eventItems;
        }).flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<EventDto> findById(Long eventId) {
        String eventImageFolder = getEventImageFolder(eventId);
        
        Mono<EventEntity> eventMono = eventRepository.findById(eventId);
        Mono<List<Long>> eventTypeIdsMono = eventTypeReferenceRepository.findEventTypeIdsByEventId(eventId).collectList();
        Mono<List<String>> eventImagesMono = fileStorageService.getFileList(eventImageFolder).collectList();

        return Mono.zip(eventMono, eventTypeIdsMono, eventImagesMono).map(tuple -> {
            EventEntity eventEntity = tuple.getT1();
            List<Long> eventTypeIds = tuple.getT2();
            List<String> eventImages = tuple.getT3();

            EventDto eventDto = eventMapper.toDto(eventEntity);
            eventDto.setEventTypeIds(eventTypeIds);
            eventDto.setEventImages(eventImages);

            return eventDto;
        });
    }

    @Override
    public Mono<Void> update(Long eventId, EventContentDto eventContentDto) {
        EventEntity eventEntity = eventMapper.toEntity(eventContentDto);
        eventEntity.setId(eventId);

        return eventRepository.update(eventEntity)
                .then(eventTypeReferenceRepository.update(eventId, eventContentDto.getEventTypeIds()));
    }

    @Override
    public Mono<Void> remove(Long eventId) {
        String eventImageFolder = getEventImageFolder(eventId);

        return eventRepository.remove(eventId).then(fileStorageService.remove(eventImageFolder));
    }

    @Override
    public Mono<Void> uploadImage(Long eventId, FilePart eventImageFilePart) {
        String eventImageFileName = generateEventImageFileName(eventId, eventImageFilePart);

        return fileStorageService.saveFile(eventImageFileName, eventImageFilePart);
    }

    @Override
    public  Flux<DataBuffer> readImage(Long eventId, String imageName) {
        String eventImageFolder = getEventImageFolder(eventId);
        String eventImageFileName = eventImageFolder + imageName;

        return fileStorageService.readFile(eventImageFileName, EVENT_IMAGE_NOT_FOUND_EXCEPTION);
    }

    public Mono<Void> removeImage(Long eventId, String imageName) {
        String eventImageFolder = getEventImageFolder(eventId);
        String eventImageFileName = eventImageFolder + imageName;

        return fileStorageService.remove(eventImageFileName);
    }

    private String generateEventImageFileName(Long eventId, FilePart eventImageFilePart) {
        String originalFileName = eventImageFilePart.filename();
        String eventImageExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String eventImageFolder = getEventImageFolder(eventId);

        return eventImageFolder + UUID.randomUUID().toString().toUpperCase() + eventImageExtension;
    }

    private String getEventImageFolder(Long eventId) {
        return String.format("%s/%d/", EVENT_IMAGE_FOLDER, eventId);
    }
}
