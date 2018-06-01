package io.example.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.UserApplication;
import io.example.domain.nodes.Event;
import io.example.domain.nodes.User;
import io.example.domain.rels.Action;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.cloud.contract.wiremock.restdocs.SpringCloudContractRestDocs;
import org.springframework.http.MediaType;
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
@SpringBootTest(classes = UserApplication.class,
		properties = {
		"eureka.client.enabled=false"
		})
@AutoConfigureRestDocs(outputDir = "target/snippets")
@AutoConfigureMockMvc
public class ActionRepositoryTests {

	@Autowired MockMvc mockMvc;
	@Autowired ActionRepository repository;

	@ClassRule
	public static GenericContainer neo4j =
			new GenericContainer("neo4j:3.4.0")
					.withExposedPorts(7474, 7473)
					.withEnv("NEO4J_AUTH", "none");

	@BeforeClass
	public static void setupClass() {
		System.setProperty("spring.data.neo4j.uri", "http://" + neo4j.getContainerIpAddress() + ":" + neo4j.getMappedPort(7474));
	}

	@Test
	public void should_get_action() throws Exception {
		repository.save(action());

		mockMvc.perform(MockMvcRequestBuilders.get("/actions"))
				.andExpect(status().is2xxSuccessful())
				.andDo(MockMvcRestDocumentation.document("action",
						SpringCloudContractRestDocs.dslContract()));
	}

	private Action action() {
		Action action = new Action();
		action.setId(1L);
		action.setActor(user());
		action.setEvent(event());
		return action;
	}

	private User user() {
		User user = new User();
		user.setId(1L);
		user.setEmail("foo@bar.com");
		user.setFirstName("Foo");
		user.setLastName("Bar");
		user.setPhone("123123123");
		return user;
	}

	private Event event() {
		Event event = new Event();
		event.setUser(user());
		return event;
	}

}