package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.recommendationconfigurations;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.commons.infrastructure.adapters.incoming.rest.BadRequestException;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationConfigurationManager;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.UpdateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class UpdateRecommendationConfigurationController {

	private final RecommendationConfigurationManager recommendationConfigurationManager;

	@PutMapping("/api/recommendations/configuration")
	public ResponseEntity<?> createRecommendationConfiguration(@RequestHeader("userId") final Integer userId,
															   @RequestBody final UpdateRecommendationConfigurationDto dto) {
		recommendationConfigurationManager.updateRecommendationConfiguration(createCommand(userId, dto));
		return ResponseEntity.status(200).build();
	}

	private UpdateRecommendationConfiguration createCommand(final Integer userId, final UpdateRecommendationConfigurationDto dto) {
		try {
			return new UpdateRecommendationConfiguration(
					dto.getGenres().stream().map(MovieGenre::valueOf).collect(Collectors.toSet()),
					new UserId(userId)
			);
		} catch (RuntimeException e) {
			throw new BadRequestException(e.getMessage());
		}
	}

	@Data
	private static class UpdateRecommendationConfigurationDto {

		private List<String> genres;

	}

}
