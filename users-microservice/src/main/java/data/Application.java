package data;

import com.couchbase.client.CouchbaseClient;
import data.domain.nodes.User;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.couchbase.cache.CouchbaseCacheManager;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.cache.Cache;
import org.springframework.hateoas.ResourceProcessor;

import java.util.HashMap;

@SpringBootApplication
@EnableNeo4jRepositories
@EnableDiscoveryClient
@EnableZuulProxy
@EnableHystrix
public class Application extends Neo4jConfiguration {

    public Application() {
        setBasePackage("data");
    }

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory().newEmbeddedDatabase("target/test3.db");
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

        RepositoryRestConfiguration restConfiguration = ctx.getBean("config", RepositoryRestConfiguration.class);
        restConfiguration.exposeIdsFor(User.class);
    }

    @Bean
    public ResourceProcessor<Resource<User>> movieProcessor() {
        return new ResourceProcessor<Resource<User>>() {
            @Override
            public Resource<User> process(Resource<User> resource) {

                resource.add(new Link("/movie/movies", "movies"));
                return resource;
            }
        };
    }
}
