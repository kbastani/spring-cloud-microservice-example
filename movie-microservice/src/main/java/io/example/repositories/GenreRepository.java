package io.example.repositories;

import io.example.domain.Genre;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

//@RepositoryRestResource(collectionResourceRel = "genres", path = "genres")
public interface GenreRepository extends GraphRepository<Genre> {
    List<Genre> findByName(@Param("name") String name);
}
