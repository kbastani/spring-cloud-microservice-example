package data.repositories;

import data.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(itemResourceRel = "genre", collectionResourceRel = "genres", path = "genres", excerptProjection = GenreProjection.class)
public interface GenreRepository extends JpaRepository<Genre, Long>, JpaSpecificationExecutor {
    List<Genre> findByName(@Param("name") String name);
}
