package org.lviv.crime.entity.event;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Data
@Table("events")
public class EventEntity {
    @Id
    private Long id;

    private String title;

    private String description;

    private Timestamp eventDate;

    @ReadOnlyProperty
    private Timestamp publicationDate;
}
