package org.lviv.crime.controller.event;

import lombok.AllArgsConstructor;
import org.lviv.crime.auth.Auth;
import org.lviv.crime.dto.event.EventContentDto;
import org.lviv.crime.dto.event.EventDto;
import org.lviv.crime.dto.event.EventItemDto;
import org.lviv.crime.service.event.EventService;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.lviv.crime.constants.Constants.IMAGE_FILE_REQUEST_PARAM;

@RestController
@AllArgsConstructor
@RequestMapping("events")
public class EventController {

    private final EventService eventService;

    @Auth
    @PostMapping
    public Mono<EventDto> create(@RequestBody EventContentDto eventContentDto) {
        return eventService.create(eventContentDto);
    }

    @GetMapping
    public Flux<EventItemDto> findAll() {
        return eventService.findAllItems();
    }

    @GetMapping("{eventId}")
    public Mono<EventDto> find(@PathVariable Long eventId) {
        return eventService.findById(eventId);
    }

    @Auth
    @PutMapping("{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(@PathVariable Long eventId, @RequestBody EventContentDto eventContentDto) {
        return eventService.update(eventId, eventContentDto);
    }

    @Auth
    @DeleteMapping("{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> remove(@PathVariable Long eventId) {
        return eventService.remove(eventId);
    }

    @PostMapping(value = "{eventId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> uploadImage(@PathVariable Long eventId,
                                       @RequestPart(IMAGE_FILE_REQUEST_PARAM) FilePart eventImageFilePart) {

        return eventService.uploadImage(eventId, eventImageFilePart);
    }

    @GetMapping(value = "{eventId}/image/{imageName}", produces = {
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_GIF_VALUE
    })
    public Flux<DataBuffer> readEventImage(@PathVariable Long eventId, @PathVariable String imageName) {
        return eventService.readImage(eventId, imageName);
    }

    @DeleteMapping("{eventId}/image/{imageName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> removeImage(@PathVariable Long eventId, @PathVariable String imageName) {
        return eventService.removeImage(eventId, imageName);
    }
}
