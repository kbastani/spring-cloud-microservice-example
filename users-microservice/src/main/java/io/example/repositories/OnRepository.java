package io.example.repositories;

import io.example.domain.rels.On;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "on", path = "on")
public interface OnRepository extends GraphRepository<On> {

}
