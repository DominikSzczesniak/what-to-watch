package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.recommendationconfigurations;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationConfigurationManager;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.UpdateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Set;

import static pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.recommendationconfigurations.CreateMovieGenreSet.createGenreSet;

@RequiredArgsConstructor
@RestController
public class UpdateRecommendationConfigurationController {

	private final RecommendationConfigurationManager recommendationConfigurationManager;

	@PutMapping("/api/recommendations/configuration")
	public ResponseEntity<?> updateRecommendationConfiguration(@RequestHeader("userId") final Integer userId,
															   @RequestBody final UpdateRecommendationConfigurationDto dto) {
		recommendationConfigurationManager.update(
				new UpdateRecommendationConfiguration(createGenreSet(dto.getGenres()), new UserId(userId)));
		return ResponseEntity.status(200).build();
	}

	@Data
	private static class UpdateRecommendationConfigurationDto {

		private List<String> genres;

	}

}
