DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE event_types
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL
);

CREATE TABLE events
(
    id               BIGSERIAL PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    description      TEXT         NOT NULL,
    event_date       TIMESTAMP,
    publication_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE event_type_references
(
    event_id      BIGINT NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    event_type_id BIGINT NOT NULL REFERENCES event_types (id),
    PRIMARY KEY (event_id, event_type_id)
);

CREATE TABLE users
(
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(32) UNIQUE NOT NULL,
    password_hash VARCHAR(128)       NOT NULL,
    first_name    VARCHAR(32)        NOT NULL,
    middle_name   VARCHAR(32)        NOT NULL,
    last_name     VARCHAR(32)        NOT NULL,
    email         VARCHAR(32) UNIQUE NOT NULL,
    phone         VARCHAR(16),
    info          TEXT
);