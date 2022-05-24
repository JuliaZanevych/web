package org.lviv.crime.entity.event.type.reference;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("event_type_references")
public class EventTypeReferenceEntity {
    private Long eventId;

    private Long eventTypeId;
}
