package data.domain.rels;

import data.domain.nodes.User;
import data.domain.nodes.Product;
import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type="ON")
public class Rated {
    @GraphId
    Long id;
    @StartNode
    Product event;
    @EndNode
    User job;

    public Rated() {
    }

    public Product getEvent() {
        return event;
    }

    public void setEvent(Product event) {
        this.event = event;
    }

    public User getJob() {
        return job;
    }

    public void setJob(User job) {
        this.job = job;
    }
}
