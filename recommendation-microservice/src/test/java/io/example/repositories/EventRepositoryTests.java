package io.example.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.Application;
import io.example.domain.nodes.Event;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.cloud.contract.wiremock.restdocs.SpringCloudContractRestDocs;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.GenericContainer;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
		properties = {
		"eureka.client.enabled=false"
		})
@AutoConfigureRestDocs(outputDir = "target/snippets")
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class EventRepositoryTests {

	@Autowired MockMvc mockMvc;
	@Autowired EventRepository repository;

	@ClassRule
	public static GenericContainer neo4j =
			new GenericContainer("neo4j:3.4.0")
					.withExposedPorts(7474, 7473)
					.withEnv("NEO4J_AUTH", "none");

	@BeforeClass
	public static void setupClass() {
		System.setProperty("spring.data.neo4j.uri", "http://" + neo4j.getContainerIpAddress() + ":" + neo4j.getMappedPort(7474));
	}

	@Before
	public void setup() {
		ObjectMapper objectMappper = new ObjectMapper();
		// Possibly configure the mapper
		JacksonTester.initFields(this, objectMappper);
	}

	@Test
	public void should_get_events() throws Exception {
		repository.save(new Event());
		repository.save(new Event());

		mockMvc.perform(MockMvcRequestBuilders.get("/events"))
				.andExpect(status().is2xxSuccessful())
				.andDo(MockMvcRestDocumentation.document("events",
						SpringCloudContractRestDocs.dslContract()));
	}

}