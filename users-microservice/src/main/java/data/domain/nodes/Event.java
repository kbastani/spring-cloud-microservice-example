package data.domain.nodes;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Event {
    @GraphId
    private Long id;
    private String eventType;

    @Fetch
    @RelatedTo(type = "ACTION", direction = Direction.INCOMING)
    User user;

    @Fetch
    @RelatedTo(type = "ON", direction = Direction.OUTGOING)
    Content content;

    public Event() {
    }

    public Event(String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
