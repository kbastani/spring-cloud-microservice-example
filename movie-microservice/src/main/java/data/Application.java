package data;

import data.domain.nodes.Genre;
import data.domain.nodes.Movie;
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
		return new GraphDatabaseFactory().newEmbeddedDatabase("target/movie.db");
	}

	public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

        RepositoryRestConfiguration restConfiguration = ctx.getBean("config", RepositoryRestConfiguration.class);
        restConfiguration.exposeIdsFor(Movie.class);
        restConfiguration.exposeIdsFor(Genre.class);
	}

    @Bean
    public CommandLineRunner commandLineRunner() {
        return strings -> {
            // Import graph data for users
            String movieImport = "LOAD CSV WITH HEADERS FROM \"http://localhost:9005/movies.csv\" AS csvLine\n" +
                    "MERGE (movie:Movie:_Movie { id: csvLine.id, title: csvLine.title, released: csvLine.timestamp, url: csvLine.url })";

            neo4jTemplate().query(movieImport, null).finish();

            String genreImport = "LOAD CSV WITH HEADERS FROM \"http://localhost:9005/movie-genres.csv\" AS csvLine\n" +
                    "MATCH (movie:Movie { id: csvLine.id })\n" +
                    "WITH movie, split(csvLine.genres,\";\") as genres\n" +
                    "FOREACH(g in genres |\n" +
                    "    MERGE (genre:Genre:_Genre { name: g })\n" +
                    "    MERGE (movie)-[:HAS_GENRE]->(genre))";

            neo4jTemplate().query(genreImport, null).finish();
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
