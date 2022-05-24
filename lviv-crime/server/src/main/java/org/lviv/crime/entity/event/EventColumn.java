package org.lviv.crime.entity.event;

import java.util.stream.Stream;

public enum EventColumn {
    id,
    title,
    description,
    eventDate,
    publicationDate;

    public static final String[] ALL_COLUMN_NAMES = Stream.of(EventColumn.values())
            .map(v -> v.name()).toArray(String[]::new);
}
