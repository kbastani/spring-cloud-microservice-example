package service;

import data.domain.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.hateoas.hal.Jackson2HalModule;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = { "config", "data" })
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private RepositoryRestMvcConfiguration repositoryRestConfiguration;

    @Autowired
    private RepositoryRestConfiguration restConfiguration;

    @PostConstruct
    public void postConstructConfiguration() {
        repositoryRestConfiguration.objectMapper().registerModule(new Jackson2HalModule());
        restConfiguration.exposeIdsFor(Movie.class);
    }
}
