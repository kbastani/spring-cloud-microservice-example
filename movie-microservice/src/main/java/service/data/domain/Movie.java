package service.data.domain;

import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movie")
public class Movie implements Serializable {

    private static final long serialVersionUID = -2952735933715107252L;

    @Id
    @Column(name = "id")
    @GeneratedValue
    Long id;

    @Column
    String title;

    @Column
    Long released;

    @Column
    String url;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "movie_genre",
        inverseJoinColumns = {
                @JoinColumn
                (name = "genres_id", referencedColumnName = "id")
        },
        joinColumns = {
                @JoinColumn
                (name = "movies_id", referencedColumnName = "id")
        })
    @RestResource(exported = true)
    List<Genre> genres = new ArrayList<Genre>();

    public Movie() {
        this(null);
    }

    public Movie(Long id) {
        this.setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getReleased() {
        return released;
    }

    public void setReleased(Long released) {
        this.released = released;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Genre> getGenres() {

        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
