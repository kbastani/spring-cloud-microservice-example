package io.example.repositories;


import io.example.domain.rels.Action;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "actions", path = "actions")
public interface ActionRepository extends GraphRepository<Action> {

}
