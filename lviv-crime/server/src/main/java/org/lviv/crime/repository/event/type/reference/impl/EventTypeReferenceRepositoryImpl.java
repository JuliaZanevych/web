package org.lviv.crime.repository.event.type.reference.impl;

import lombok.AllArgsConstructor;
import org.lviv.crime.entity.event.type.reference.EventTypeReferenceColumn;
import org.lviv.crime.entity.event.type.reference.EventTypeReferenceEntity;
import org.lviv.crime.repository.event.type.reference.EventTypeReferenceRepository;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.r2dbc.core.DatabaseClient.GenericExecuteSpec;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.data.relational.core.query.Query.empty;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@AllArgsConstructor
public class EventTypeReferenceRepositoryImpl implements EventTypeReferenceRepository {

    private static final String INSERT_EVENTS_QUERY_TEMPLATE =
            "INSERT INTO event_type_references (event_id, event_type_id) VALUES ($1, $2)";

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Mono<Void> create(Long eventId, List<Long> eventTypeIds) {
        if (CollectionUtils.isEmpty(eventTypeIds)) {
            return Mono.just(false).then();
        }

        int p = 3;
        StringBuilder insertEventsQuerySb = new StringBuilder(INSERT_EVENTS_QUERY_TEMPLATE);
        for (int i = 1; i < eventTypeIds.size(); ++i) {
            insertEventsQuerySb.append(", ($");
            insertEventsQuerySb.append(p++);
            insertEventsQuerySb.append(", $");
            insertEventsQuerySb.append(p++);
            insertEventsQuerySb.append(")");
        }

        insertEventsQuerySb.append("ON CONFLICT DO NOTHING");

        String insertEventsQuery = insertEventsQuerySb.toString();

        GenericExecuteSpec insertExecuteSpec = r2dbcEntityTemplate.getDatabaseClient().sql(insertEventsQuery);

        int i = 0;
        for (Long eventTypeId : eventTypeIds) {
            insertExecuteSpec = insertExecuteSpec.bind(i++, eventId).bind(i++, eventTypeId);
        }

        return insertExecuteSpec.then();
    }

    @Override
    public Flux<EventTypeReferenceEntity> findAll() {
        return r2dbcEntityTemplate.select(EventTypeReferenceEntity.class)
                .matching(empty().columns(
                        EventTypeReferenceColumn.ALL_COLUMN_NAMES
                )).all();
    }

    @Override
    public Flux<Long> findEventTypeIdsByEventId(Long eventId) {
        return r2dbcEntityTemplate.select(EventTypeReferenceEntity.class)
                .matching(query(
                        Criteria.where(EventTypeReferenceColumn.eventId.name()).is(eventId)
                ).columns(
                        EventTypeReferenceColumn.eventTypeId.name()
                )).all().map(EventTypeReferenceEntity::getEventTypeId);
    }

    @Override
    public Mono<Void> update(Long eventId, List<Long> eventTypeIds) {
        if (CollectionUtils.isEmpty(eventTypeIds)) {
            return Mono.just(false).then();
        }

        return removeUnusedEventTypeReference(eventId, eventTypeIds)
                .then(create(eventId, eventTypeIds));
    }

    @Override
    public Mono<Void> remove(Long eventId) {
        return r2dbcEntityTemplate.delete(EventTypeReferenceEntity.class)
                .matching(query(
                        Criteria.where(EventTypeReferenceColumn.eventId.name()).is(eventId)
                )).all().then();
    }

    private Mono<Void> removeUnusedEventTypeReference(Long eventId, List<Long> eventTypeIds) {
        return r2dbcEntityTemplate.delete(EventTypeReferenceEntity.class)
                .matching(query(
                        Criteria.where(EventTypeReferenceColumn.eventId.name()).is(eventId)
                                .and(EventTypeReferenceColumn.eventTypeId.name()).notIn(eventTypeIds)
                )).all().then();
    }
}
