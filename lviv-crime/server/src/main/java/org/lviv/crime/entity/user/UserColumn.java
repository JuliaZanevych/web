package org.lviv.crime.entity.user;

import java.util.stream.Stream;

public enum UserColumn {
    id,
    username,
    passwordHash,
    firstName,
    middleName,
    lastName,
    email,
    phone,
    info;

    public static final String[] ALL_COLUMN_NAMES = Stream.of(UserColumn.values())
            .map(v -> v.name()).toArray(String[]::new);
}
