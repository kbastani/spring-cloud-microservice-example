package data.repositories;

import data.domain.rels.Follows;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "follows", path = "follows")
public interface FollowsRepository extends PagingAndSortingRepository<Follows, Long> {

}
