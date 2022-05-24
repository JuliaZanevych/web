package org.lviv.crime.entity.event.type;

import java.util.stream.Stream;

public enum EventTypeColumn {
    id,
    name;

    public static final String[] ALL_COLUMN_NAMES = Stream.of(EventTypeColumn.values())
            .map(v -> v.name()).toArray(String[]::new);
}
