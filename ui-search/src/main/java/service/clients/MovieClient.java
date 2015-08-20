package service.clients;

import service.models.Movie;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("movie")
public interface MovieClient {
    @RequestMapping(method = RequestMethod.GET, value = "/movies")
    PagedResources<Movie> findAll();

    @RequestMapping(method = RequestMethod.GET, value = "/movies/search/findByTitleContainingIgnoreCase?title={title}")
    PagedResources<Movie> findByTitleContainingIgnoreCase(@PathVariable("title") String title);

    @RequestMapping(method = RequestMethod.GET, value = "/movies/{id}")
    List<Movie> findById(@PathVariable("id") String id);

    @RequestMapping(method = RequestMethod.GET, value = "/movies/search/findByIdIn?ids={ids}")
    PagedResources<Movie> findByIds(@PathVariable("ids") String ids);

    @RequestMapping(method = RequestMethod.POST, value = "/movies",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    void createMovie(@RequestBody Movie movie);
}
