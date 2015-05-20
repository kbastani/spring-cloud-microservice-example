package data.repositories;


import data.domain.rels.Action;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "actions", path = "actions")
public interface ActionRepository extends PagingAndSortingRepository<Action, Long> {

}
