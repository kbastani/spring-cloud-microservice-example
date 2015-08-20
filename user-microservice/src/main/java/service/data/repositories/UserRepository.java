package service.data.repositories;

import service.data.domain.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.neo4j.repository.GraphRepository;
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

    List<User> findByZipcode(@Param("4") String zipcode);
}
