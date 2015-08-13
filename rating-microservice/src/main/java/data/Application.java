package data;

import config.GraphDatabaseConfiguration;
import data.domain.nodes.Product;
import data.domain.nodes.User;
import data.domain.rels.Rating;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
@ComponentScan(basePackages = {"data", "config"})
@EnableNeo4jRepositories
@EnableTransactionManagement(mode = AdviceMode.PROXY)
@Slf4j
public class Application {

    final Logger logger = LoggerFactory.getLogger(Application.class);

    // Used to bootstrap the Neo4j database with demo data
    @Value("${aws.s3.url}")
    String datasetUrl;

    public static void main(String[] args) {
        System.setProperty("org.neo4j.rest.read_timeout", "250");

        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

        // This tells the Spring Data REST repositories to expose the ID of entities
        RepositoryRestConfiguration restConfiguration = ctx.getBean("config", RepositoryRestConfiguration.class);
        restConfiguration.exposeIdsFor(User.class);
        restConfiguration.exposeIdsFor(Product.class);
        restConfiguration.exposeIdsFor(Rating.class);
    }

    /**
     * Bootstrap the Neo4j database with demo dataset. This can run multiple times without
     * duplicating data.
     *
     * @param graphDatabaseConfiguration is the graph database configuration to communicate with the Neo4j server
     * @return a {@link CommandLineRunner} instance with the method delegate to execute
     */
    @Bean
    public CommandLineRunner commandLineRunner(GraphDatabaseConfiguration graphDatabaseConfiguration) {
        return strings -> {
            logger.info("Creating index on User(id) and Product(id)...");
            graphDatabaseConfiguration.neo4jTemplate().query("CREATE INDEX ON :User(id)", null).finish();
            graphDatabaseConfiguration.neo4jTemplate().query("CREATE INDEX ON :Product(id)", null).finish();
            logger.info("Importing ratings data...");

            // Import graph data for users
            String userImport = String.format("USING PERIODIC COMMIT 20000\n" +
                    "LOAD CSV WITH HEADERS FROM \"%s/ratings.csv\" AS csvLine\n" +
                    "MERGE (user:User:_User { id: toInt(csvLine.userId) })\n" +
                    "ON CREATE SET user.__type__=\"User\", user.className=\"data.domain.nodes.User\", user.knownId = csvLine.userId\n" +
                    "MERGE (product:Product:_Product { id: toInt(csvLine.movieId) })\n" +
                    "ON CREATE SET product.__type__=\"Product\", product.className=\"data.domain.nodes.Product\", product.knownId = csvLine.movieId\n" +
                    "MERGE (user)-[r:Rating]->(product)\n" +
                    "ON CREATE SET r.timestamp = toInt(csvLine.timestamp), r.rating = toInt(csvLine.rating), r.knownId = csvLine.userId + \"_\" + csvLine.movieId, r.__type__ = \"Rating\", r.className = \"data.domain.rels.Rating\"", datasetUrl);


            graphDatabaseConfiguration.neo4jTemplate().query(userImport, null).finish();
            logger.info("Import complete");
        };

    }

    @Bean
    public ResourceProcessor<Resource<User>> resourceProcessor() {
        return new ResourceProcessor<Resource<User>>() {
            @Override
            public Resource<User> process(Resource<User> resource) {
                try {
                    // Add a link to the resource to get the ratings of a user
                    resource.add(new Link(new URI(resource.getLink("self").getHref())
                            .resolve(String.format("/ratings/search/users?id=%s",
                                    resource.getContent().getId())).toString(), "ratings"));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                return resource;
            }
        };
    }


}
