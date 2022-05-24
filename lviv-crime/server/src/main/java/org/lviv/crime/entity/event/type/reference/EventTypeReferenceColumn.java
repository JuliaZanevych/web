package org.lviv.crime.entity.event.type.reference;

import java.util.stream.Stream;

public enum EventTypeReferenceColumn {
    eventId,
    eventTypeId;

    public static final String[] ALL_COLUMN_NAMES = Stream.of(EventTypeReferenceColumn.values())
            .map(v -> v.name()).toArray(String[]::new);
}
