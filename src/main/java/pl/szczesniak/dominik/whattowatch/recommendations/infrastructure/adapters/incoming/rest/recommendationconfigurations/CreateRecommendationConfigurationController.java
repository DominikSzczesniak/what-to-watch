package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendationconfigurations;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationConfigurationManager;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
public class CreateRecommendationConfigurationController {

	private final RecommendationConfigurationManager recommendationConfigurationManager;

	@PutMapping("/api/recommendations/configuration")
	public ResponseEntity<?> createRecommendationConfiguration(@RequestHeader("userId") final Integer userId,
															   @RequestBody final RecommendationConfigurationDto recommendationConfigurationDto) {
		final Set<MovieGenre> movieGenres = new HashSet<>();

		final List<String> genres = recommendationConfigurationDto.getGenres();
		for (String genre : genres) {
			try {
				final MovieGenre movieGenre = MovieGenre.valueOf(genre);
				movieGenres.add(movieGenre);
			} catch (IllegalArgumentException e) {
				return ResponseEntity.status(400).body("Invalid genre: " + genre);
			}
		}

		final ConfigurationId configurationId = recommendationConfigurationManager.create(new UserId(userId), movieGenres);
		return ResponseEntity.status(201).body(configurationId);
	}

	@Data
	private static class RecommendationConfigurationDto {

		private List<String> genres;

	}

}
