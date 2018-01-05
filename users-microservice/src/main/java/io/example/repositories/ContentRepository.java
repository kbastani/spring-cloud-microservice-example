package io.example.repositories;

import io.example.domain.nodes.Content;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "content", path = "content")
public interface ContentRepository extends GraphRepository<Content> {
    List<Content> findByTitle(@Param("title") String title);
}
