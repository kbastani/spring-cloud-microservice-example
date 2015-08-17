package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.hateoas.hal.Jackson2HalModule;
import service.data.domain.Movie;

import javax.annotation.PostConstruct;

@SpringBootApplication
@ComponentScan(basePackages = { "service.config", "service.data" })
@EnableZuulProxy
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private RepositoryRestMvcConfiguration repositoryRestConfiguration;

    @PostConstruct
    public void postConstructConfiguration() {
        repositoryRestConfiguration.objectMapper().registerModule(new Jackson2HalModule());
        repositoryRestConfiguration.config().exposeIdsFor(Movie.class);
    }
}
