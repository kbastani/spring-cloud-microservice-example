package data.repositories;

import data.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(itemResourceRel = "movie",
        collectionResourceRel = "movies", path = "movies", excerptProjection = MovieProjection.class)
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor {
    List<Movie> findByTitle(@Param("title") String title);
}
