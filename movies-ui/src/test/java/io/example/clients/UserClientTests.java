package io.example.clients;

import io.example.MoviesUiApplication;
import io.example.models.User;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.PagedResources;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserClientTests.Config.class,
		properties = {
				"eureka.client.enabled=false",
				"stubrunner.idsToServiceIds[users-microservice]=user"
		})
@AutoConfigureStubRunner(ids = "io.example:users-microservice", workOffline = true)
public class UserClientTests {

	@Autowired UserClient userClient;

	@Test
	public void should_fetch_user() {
		PagedResources<User> users = userClient.findAll();

		BDDAssertions.then(users).isNotNull();
	}

	@Configuration
	@EnableAutoConfiguration
	@EnableFeignClients
	static class Config {

	}
}
