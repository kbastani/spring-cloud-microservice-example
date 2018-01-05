package io.example.domain.rels;

import io.example.domain.nodes.User;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "FOLLOWS")
public class Follows {

    @GraphId
    private Long id;

    @StartNode
    private User follower;

    @EndNode
    private User follows;

    public Follows() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollows() {
        return follows;
    }

    public void setFollows(User follows) {
        this.follows = follows;
    }
}
