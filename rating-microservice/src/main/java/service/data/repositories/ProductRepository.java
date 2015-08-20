package service.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import service.data.domain.entity.Product;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Override
    @Query("MATCH (p:Product) RETURN p")
    Page<Product> findAll(Pageable pageable);

    @RestResource(path = "average", rel = "movies")
    @Query(value = "MATCH ()-[r:Rating]->(p:Product) WHERE p.knownId = {id} RETURN avg(toFloat(r.rating))")
    Double getAverageRating(@Param(value = "id") String id);

    @Query(value = "MATCH (u:User)-[r:Rating]->(p:Product) WHERE u.knownId = {id} RETURN p")
    Page<Product> findProductsByUser(@Param(value = "id") String id, Pageable pageable);

    @Override
    @Query("MATCH (p:Product) WHERE p.id = {id} RETURN p")
    Product findOne(@Param(value = "id") Long id);
}
