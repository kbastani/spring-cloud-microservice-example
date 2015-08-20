package service.clients;

import service.models.Movie;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import service.models.Product;

import java.util.List;

@FeignClient("rating")
public interface RatingClient {
    @RequestMapping(method = RequestMethod.GET, value = "/ratings")
    PagedResources<Movie> findAll();

    @RequestMapping(method = RequestMethod.GET, value = "/products/search/findProductsByUser?id={id}")
    PagedResources<Product> findProductsByUser(@PathVariable("id") String id);

    @RequestMapping(method = RequestMethod.GET, value = "/movies/{id}")
    List<Movie> findById(@PathVariable("id") String id);

    @RequestMapping(method = RequestMethod.POST, value = "/movies",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    void createMovie(@RequestBody Movie movie);
}
