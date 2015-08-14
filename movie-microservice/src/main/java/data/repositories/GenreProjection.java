package data.repositories;

import data.domain.Genre;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "genre", types = Genre.class)
public interface GenreProjection {
    Long getId();
    String getName();
}
