package org.lviv.crime.service.event;

import org.lviv.crime.dto.event.EventContentDto;
import org.lviv.crime.dto.event.EventDto;
import org.lviv.crime.dto.event.EventItemDto;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventService {

    Mono<EventDto> create(EventContentDto eventContentDto);

    Flux<EventItemDto> findAllItems();

    Mono<EventDto> findById(Long eventId);

    Mono<Void> update(Long eventId, EventContentDto eventContentDto);

    Mono<Void> remove(Long eventId);

    Mono<Void> uploadImage(Long eventId, FilePart eventImageFilePart);

    Flux<DataBuffer> readImage(Long eventId, String imageName);

    Mono<Void> removeImage(Long eventId, String imageName);
}
