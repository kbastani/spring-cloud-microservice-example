package data.repositories;

import data.domain.rels.On;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "on", path = "on")
public interface OnRepository extends PagingAndSortingRepository<On, Long> {

}
