package hystrixdashboard.test;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Spencer Gibb
 */
@Configuration
@EnableAutoConfiguration(exclude = FreeMarkerAutoConfiguration.class)
@EnableHystrix
@RestController
public class SampleHystrixApp {

	@Autowired
	Service service;

	@RequestMapping("/")
	public String slash() {
		return service.hello();
	}

	@Bean
	Service service() {
		return new Service();
	}

	public static void main(String[] args) {
		SpringApplication.run(SampleHystrixApp.class, args);
	}
}
class Service {

	@HystrixCommand
	public String hello() {
		return "Hello";
	}
}
