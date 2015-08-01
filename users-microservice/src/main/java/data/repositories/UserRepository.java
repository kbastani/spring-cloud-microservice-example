package data.repositories;

import data.domain.nodes.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends GraphRepository<User> {


    @Override
    @CacheEvict(value = "cache", key = "#p0.id")
    <U extends User> U save(U entity);

    @Override
    @CacheEvict(value = "cache", key = "#p0", beforeInvocation = true)
    void delete(Long aLong);

    @Override
    @CacheEvict(value = "cache", key = "#p0.id", beforeInvocation = true)
    void delete(User entity);

    @Override
    @Cacheable(value = "cache", key = "#p0")
    User findOne(Long aLong);

    // TODO: Replace with @Param("name") when Spring Data Neo4j supports names vs. positional arguments
    List<User> findByLastName(@Param("0") String name);

}
