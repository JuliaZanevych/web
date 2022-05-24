package org.lviv.crime.dto.event.type;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EventTypeContentDto {
    @NotNull
    private String name;
}
