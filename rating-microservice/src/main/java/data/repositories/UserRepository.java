package data.repositories;

import data.domain.nodes.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends GraphRepository<User> {
    @Override
    @Query(value = "MATCH (n:User) RETURN n", countQuery = "MATCH (n:User) RETURN count(n)")
    Page<User> findAll(Pageable pageable);
}
