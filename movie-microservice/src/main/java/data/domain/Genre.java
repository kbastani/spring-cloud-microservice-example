package data.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Genre implements Serializable {
    private static final long serialVersionUID = -5216130437873456233L;

    @Id
    @JsonProperty("id")
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @JsonProperty("name")
    @Column(name = "name")
    private String name;

    public Genre() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
