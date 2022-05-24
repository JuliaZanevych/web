package org.lviv.crime.controller.event.type;

import lombok.AllArgsConstructor;
import org.lviv.crime.dto.event.type.EventTypeContentDto;
import org.lviv.crime.dto.event.type.EventTypeDto;
import org.lviv.crime.service.event.type.EventTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("events/types")
public class EventTypeController {

    private final EventTypeService eventTypeService;

    @PostMapping
    public Mono<EventTypeDto> create(@RequestBody EventTypeContentDto eventTypeContentDto) {
        return eventTypeService.create(eventTypeContentDto);
    }

    @GetMapping
    public Flux<EventTypeDto> findAll() {
        return eventTypeService.findAll();
    }

    @GetMapping("{eventTypeId}")
    public Mono<EventTypeDto> find(@PathVariable Long eventTypeId) {
        return eventTypeService.findById(eventTypeId);
    }

    @PutMapping("{eventTypeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(@PathVariable Long eventTypeId, @RequestBody EventTypeContentDto eventTypeContentDto) {
        return eventTypeService.update(eventTypeId, eventTypeContentDto);
    }

    @DeleteMapping("{eventTypeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> remove(@PathVariable Long eventTypeId) {
        return eventTypeService.remove(eventTypeId);
    }
}
