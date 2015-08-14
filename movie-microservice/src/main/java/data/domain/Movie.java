package data.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Movie implements Serializable {

    private static final long serialVersionUID = -5516160437873476233L;

    @Id
    @Column(name = "id")
    @GeneratedValue
    Long id;

    @JsonProperty("title")
    @Column
    String title;

    @JsonProperty("released")
    @Column
    Long released;

    @JsonProperty("url")
    @Column
    String url;

    @JsonProperty("genres")
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "movie_genre",
        inverseJoinColumns = {
                @JoinColumn
                        (name = "genres_id",
                                referencedColumnName = "id")
        },
        joinColumns = {
                @JoinColumn
                        (name = "movies_id",
                                referencedColumnName = "id")
        })
    @Column
    Set<Genre> genres = new HashSet<>();

    public Movie() {
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
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
}
