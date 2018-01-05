package io.example.repositories;

import io.example.domain.rels.Follows;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "follows", path = "follows")
public interface FollowsRepository extends GraphRepository<Follows> {

}
