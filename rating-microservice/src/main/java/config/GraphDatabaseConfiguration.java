package config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringCypherRestGraphDatabase;

/**
 * @author Kenny Bastani
 *
 * Manages the configuration for a Neo4j graph database server
 */
@EnableNeo4jRepositories
@EnableConfigurationProperties
@Configuration
@ConfigurationProperties
public class GraphDatabaseConfiguration extends Neo4jConfiguration {

    @Value("${neo4j.uri}")
    private String url;

    @Value("${neo4j.username}")
    private String username;

    @Value("${neo4j.password}")
    private String password;

    @Autowired
    Environment environment;

    public GraphDatabaseConfiguration() {
        super();
        setBasePackage("data", "config");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Autowired(required = true)
    @Override
    public void setGraphDatabaseService(GraphDatabaseService graphDatabaseService) {
        super.setGraphDatabaseService(graphDatabaseService);
    }

    @Bean
    public static YamlPropertySourceLoader yamlPropertySourceLoader()             {
        return new YamlPropertySourceLoader();
    }

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        // Connect to Neo4j
        setGraphDatabaseService(new SpringCypherRestGraphDatabase(url, username, password));
        return getGraphDatabaseService();
    }
}
