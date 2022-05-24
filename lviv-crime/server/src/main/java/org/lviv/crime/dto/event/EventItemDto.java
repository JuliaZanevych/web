package org.lviv.crime.dto.event;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class EventItemDto {

    private Long id;

    private String title;

    private List<Long> eventTypeIds;

    private String description;

    private Timestamp eventDate;

    private Timestamp publicationDate;

}
