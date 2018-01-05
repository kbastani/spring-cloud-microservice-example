package io.example.repositories;

import io.example.domain.Movie;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

//@RepositoryRestResource(collectionResourceRel = "movies", path = "movies")
public interface MovieRepository extends GraphRepository<Movie> {
    List<Movie> findByTitle(@Param("title") String title);
}
