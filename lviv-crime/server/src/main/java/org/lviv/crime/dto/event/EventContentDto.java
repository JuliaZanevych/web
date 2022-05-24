package org.lviv.crime.dto.event;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class EventContentDto {

    private String title;

    private List<Long> eventTypeIds;

    private String description;

    private Timestamp eventDate;

}
