package service.data.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "genre")
public class Genre implements Serializable {

    private static final long serialVersionUID = -1952735933715107252L;

    @Id
    @Column(name = "id")
    @GeneratedValue
    Long id;

    @Column(name = "name")
    String name;

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
