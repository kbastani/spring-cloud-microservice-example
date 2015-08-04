package data;

import data.domain.nodes.User;
import data.domain.nodes.Product;
import data.domain.rels.Rating;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@SpringBootApplication
@EnableNeo4jRepositories
@EnableDiscoveryClient
@EnableZuulProxy
@EnableHystrix
@Slf4j
public class Application extends Neo4jConfiguration {

	final Logger logger = LoggerFactory.getLogger(Application.class);

	public Application() {
		setBasePackage("data");
	}

	@Bean(destroyMethod = "shutdown")
	public GraphDatabaseService graphDatabaseService() {
		return new GraphDatabaseFactory().newEmbeddedDatabase("target/ratings.db");
	}

	public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        RepositoryRestConfiguration restConfiguration = ctx.getBean("config", RepositoryRestConfiguration.class);
        restConfiguration.exposeIdsFor(User.class);
        restConfiguration.exposeIdsFor(Product.class);
        restConfiguration.exposeIdsFor(Rating.class);
	}

    @Bean
    public CommandLineRunner commandLineRunner() {
        return strings -> {
            logger.info("Creating index on User(id) and Product(id)...");
            neo4jTemplate().query("CREATE INDEX ON :User(id)", null).finish();
            neo4jTemplate().query("CREATE INDEX ON :Product(id)", null).finish();
            logger.info("Importing ratings data...");

            // Import graph data for users
            String userImport = "USING PERIODIC COMMIT 20000\n" +
                    "LOAD CSV WITH HEADERS FROM \"http://localhost:9004/ratings.csv\" AS csvLine\n" +
                    "MERGE (user:User:_User { id: toInt(csvLine.userId) })\n" +
                    "ON CREATE SET user.__type__=\"User\", user.className=\"data.domain.nodes.User\"\n" +
                    "MERGE (product:Product:_Product { id: toInt(csvLine.movieId) })\n" +
                    "ON CREATE SET product.__type__=\"Product\", product.className=\"data.domain.nodes.Product\"\n" +
                    "MERGE (user)-[r:Rating]->(product)\n" +
                    "ON CREATE SET r.timestamp = toInt(csvLine.timestamp), r.rating = toInt(csvLine.rating), r.knownId = csvLine.userId + \"_\" + csvLine.movieId, r.__type__ = \"Rating\", r.className = \"data.domain.rels.Rating\"";

            neo4jTemplate().query(userImport, null).finish();
            logger.info("Import complete");
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
}
