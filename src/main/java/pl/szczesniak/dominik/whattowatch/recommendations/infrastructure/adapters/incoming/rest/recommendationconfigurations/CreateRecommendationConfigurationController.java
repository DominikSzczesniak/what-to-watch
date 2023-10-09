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
	public ResponseEntity<?> addCommentToMovie(@RequestHeader("userId") final Integer userId,
											   @RequestBody final RecommendationConfigurationDto recommendationConfigurationDto) {
		final Set<MovieGenre> genres = new HashSet<>();
		final List<String> genres1 = recommendationConfigurationDto.getGenres();
		for (String genre : genres1) {
			final MovieGenre movieGenre = MovieGenre.valueOf(genre);
			genres.add(movieGenre);
		}
		final ConfigurationId configurationId = recommendationConfigurationManager.create(new UserId(userId), genres);
		return ResponseEntity.status(200).body(configurationId);
	}

	@Data
	private static class RecommendationConfigurationDto {

		private List<String> genres;

	}

}
