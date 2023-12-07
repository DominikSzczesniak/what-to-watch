package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendations.recommendationconfigurations;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationConfigurationManager;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
public class GetRecommendationConfigurationController {

	private final RecommendationConfigurationManager recommendationConfigurationManager;

	@GetMapping("/api/recommendations/configuration")
	public ResponseEntity<RecommendationConfigurationDto> getRecommendationConfiguration(@RequestHeader("userId") final Integer userId) {
		final RecommendationConfiguration config = recommendationConfigurationManager.findBy(new UserId(userId));
		final RecommendationConfigurationDto configDto = new RecommendationConfigurationDto(
				config.getConfigurationId().getValue(),
				mapGenres(config.getGenres()),
				config.getUserId().getValue()
		);
		return ResponseEntity.status(200).body(configDto);
	}

	private List<String> mapGenres(final Set<MovieGenre> genres) {
		return genres.stream().map(Enum::toString).toList();
	}

	@Value
	private static class RecommendationConfigurationDto {

		Long configurationId;
		List<String> genreNames;
		Integer userId;

	}

}
