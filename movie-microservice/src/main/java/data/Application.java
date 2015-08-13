package data;

import config.GraphDatabaseConfiguration;
import data.domain.nodes.Genre;
import data.domain.nodes.Movie;
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
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = {"data", "config"})
@EnableNeo4jRepositories
@EnableTransactionManagement(mode = AdviceMode.PROXY)
public class Application {

    // Used to bootstrap the Neo4j database with demo data
    @Value("${aws.s3.url}")
    String datasetUrl;

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

        // This tells the Spring Data REST repositories to expose the ID of entities
        RepositoryRestConfiguration restConfiguration = ctx.getBean("config", RepositoryRestConfiguration.class);
        restConfiguration.exposeIdsFor(Movie.class);
        restConfiguration.exposeIdsFor(Genre.class);
    }

    /**
     * Bootstrap the Neo4j database with demo data set. This can run multiple times without
     * duplicating data.
     * @param graphDatabaseConfiguration is the graph database configuration to communicate with the Neo4j server
     * @return a {@link CommandLineRunner} instance with the method delegate to execute
     */
    @Bean
    public CommandLineRunner commandLineRunner(GraphDatabaseConfiguration graphDatabaseConfiguration) {
        return strings -> {

            // Import graph data for users
            String movieImport = String.format("LOAD CSV WITH HEADERS FROM \"%s/movies.csv\" AS csvLine\n" +
                    "MERGE (movie:Movie:_Movie { id: csvLine.id, title: csvLine.title, released: csvLine.timestamp, url: csvLine.url })", datasetUrl);

            graphDatabaseConfiguration.neo4jTemplate().query(movieImport, null).finish();

            String genreImport = String.format("LOAD CSV WITH HEADERS FROM \"%s/movie-genres.csv\" AS csvLine\n" +
                    "MATCH (movie:Movie { id: csvLine.id })\n" +
                    "WITH movie, split(csvLine.genres,\";\") as genres\n" +
                    "FOREACH(g in genres |\n" +
                    "    MERGE (genre:Genre:_Genre { name: g })\n" +
                    "    MERGE (movie)-[:HAS_GENRE]->(genre))", datasetUrl);

            graphDatabaseConfiguration.neo4jTemplate().query(genreImport, null).finish();
        };
    }
}
