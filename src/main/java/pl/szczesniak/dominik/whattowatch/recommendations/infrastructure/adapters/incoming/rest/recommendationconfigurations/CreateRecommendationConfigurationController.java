package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendationconfigurations;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationConfigurationManager;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.GenreId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class CreateRecommendationConfigurationController {

	private final RecommendationConfigurationManager recommendationConfigurationManager;

	@PostMapping("/api/recommendations/configuration")
	public ResponseEntity<?> addCommentToMovie(@RequestHeader("userId") final Integer userId,
											   @RequestBody final RecommendationConfigurationDto recommendationConfigurationDto) {
		final Set<GenreId> genres = recommendationConfigurationDto.getGenres().stream()
				.map(GenreId::new).collect(Collectors.toSet());
		final ConfigurationId configurationId = recommendationConfigurationManager.create(new UserId(userId), genres);
		return ResponseEntity.status(201).body(configurationId);
	}

	@Data
	private static class RecommendationConfigurationDto {

		private List<Long> genres;

	}

}
