package io.example.repositories;

import io.example.domain.nodes.Event;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "events", path = "events")
public interface EventRepository extends GraphRepository<Event> {
    List<Event> findByEventType(@Param("eventType") String eventType);
}
