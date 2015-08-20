package service.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import service.data.domain.entity.User;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    @Override
    @Query(value = "MATCH (n:User) RETURN n", countQuery = "MATCH (n:User) RETURN count(n)")
    Page<User> findAll(Pageable pageable);

    @Override
    @Query(value = "MATCH (n:User) WHERE n.knownId = '{id}' RETURN n")
    User findOne(@Param("id") Long id);
}
