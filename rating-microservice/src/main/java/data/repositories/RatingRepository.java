package data.repositories;

import data.domain.rels.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "ratings", path = "ratings")
public interface RatingRepository extends PagingAndSortingRepository<Rating, Long> {
    @Query(value = "MATCH (n:User)-[r:Rating]->() RETURN r")
    Page<Rating> findAll(Pageable pageable);
}
