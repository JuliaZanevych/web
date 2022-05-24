package org.lviv.crime.dto.event;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class EventDto extends EventContentDto {

    private Long id;

    private Timestamp publicationDate;

    private List<String> eventImages;

}
