package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.recommendationconfigurations;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.commons.infrastructure.adapters.incoming.rest.BadRequestException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationConfigurationManager;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.CreateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class CreateRecommendationConfigurationController {

	private final RecommendationConfigurationManager recommendationConfigurationManager;

	@PostMapping("/api/recommendations/configuration")
	public ResponseEntity<?> createRecommendationConfiguration(@RequestHeader("userId") final Integer userId,
															   @RequestBody final RecommendationConfigurationDto dto) {
		final ConfigurationId configurationId = recommendationConfigurationManager.create(createCommand(userId, dto));
		return ResponseEntity.status(201).body(configurationId.getValue());
	}

	private CreateRecommendationConfiguration createCommand(final Integer userId, final RecommendationConfigurationDto dto) {
		try {
			return new CreateRecommendationConfiguration(
					dto.getGenres().stream().map(MovieGenre::valueOf).collect(Collectors.toSet()),
					new UserId(userId)
			);
		} catch (RuntimeException e) {
			throw new BadRequestException(e.getMessage());
		}
	}

	@Data
	private static class RecommendationConfigurationDto {

		private List<String> genres;

	}

}
