package data;

import data.domain.nodes.User;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
        return new GraphDatabaseFactory().newEmbeddedDatabase("target/ephemeral2.db");
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

        RepositoryRestConfiguration restConfiguration = ctx.getBean("config", RepositoryRestConfiguration.class);
        restConfiguration.exposeIdsFor(User.class);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return strings -> {
            // Import graph data for users
            String userImport = "LOAD CSV WITH HEADERS FROM \"http://localhost:9000/users.csv\" AS csvLine\n" +
                    "MERGE (user:User:_User { id: csvLine.id, age: toInt(csvLine.age), gender: csvLine.gender, occupation: csvLine.occupation, zipcode: csvLine.zipcode })";

            neo4jTemplate().query(userImport, null).finish();
        };
    }

    private String openFile(String path) throws IOException {

        ClassLoader classLoader = getClass().getClassLoader();

        File file = new File(classLoader.getResource(path).getFile());

        String everything;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        }

        return everything;
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
