package data.repositories;

import data.domain.Genre;
import data.domain.Movie;
import org.springframework.data.rest.core.config.Projection;

import java.util.Set;

@Projection(name = "movie", types = { Movie.class})
public interface MovieProjection {
    String getTitle();

    Set<Genre> getGenres();
}
