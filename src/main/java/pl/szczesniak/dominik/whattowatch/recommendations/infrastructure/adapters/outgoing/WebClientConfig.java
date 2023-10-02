package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	private final String baseUrl;

	public WebClientConfig(@Value("${tmdb.base.url}") String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Bean
	public WebClient webClient() {
		return WebClient.builder().baseUrl(baseUrl).build();
	}

}
