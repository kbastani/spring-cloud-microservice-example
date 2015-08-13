package data.repositories;

import data.domain.rels.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "ratings", path = "ratings")
public interface RatingRepository extends PagingAndSortingRepository<Rating, Long> {
    @Query(value = "MATCH (n:User)-[r:Rating]->() RETURN r")
    Page<Rating> findAll(Pageable pageable);

    @RestResource(path = "users", rel = "users")
    @Query(value = "MATCH (n:User)-[r:Rating]->() WHERE id(n) = {id} RETURN r")
    Iterable<Rating> findByUserId(@Param(value = "id") Long id);
}
