package data.repositories;

import data.domain.nodes.Job;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "job", path = "job")
public interface JobRepository extends PagingAndSortingRepository<Job, Long> {
    List<Job> findByTitle(@Param("0") String title);
}
