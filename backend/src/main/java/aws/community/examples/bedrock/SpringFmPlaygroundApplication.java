package aws.community.examples.bedrock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SpringFmPlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringFmPlaygroundApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// Enable CORS for all domains, remember to change this for production purposes
				registry.addMapping("/**").allowedOrigins("*");
			}
		};
	}

}
