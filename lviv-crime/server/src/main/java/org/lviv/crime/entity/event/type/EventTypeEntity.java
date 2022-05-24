package org.lviv.crime.entity.event.type;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("event_types")
public class EventTypeEntity {
    @Id
    private Long id;

    private String name;
}
