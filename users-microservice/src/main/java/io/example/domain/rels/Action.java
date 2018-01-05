package io.example.domain.rels;

import io.example.domain.nodes.Event;
import io.example.domain.nodes.User;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "ACTION")
public class Action {
    @GraphId
    private Long id;

    @StartNode
    private User actor;

    @EndNode
    private Event event;

    public Action() {
    }

    public Action(User actor, Event event) {
        this.actor = actor;
        this.event = event;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getActor() {
        return actor;
    }

    public void setActor(User actor) {
        this.actor = actor;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

}
