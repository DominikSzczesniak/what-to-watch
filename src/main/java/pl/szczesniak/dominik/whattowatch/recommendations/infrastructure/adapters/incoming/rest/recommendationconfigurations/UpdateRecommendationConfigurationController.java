package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendationconfigurations;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationFacade;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.UpdateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@RestController
public class UpdateRecommendationConfigurationController {

	private final RecommendationFacade facade;

	@PutMapping("/api/users/recommendations/configuration")
	public ResponseEntity<?> updateRecommendationConfiguration(@RequestHeader("userId") final Integer userId,
															   @RequestBody final UpdateRecommendationConfigurationDto dto) {
		facade.update(
				new UpdateRecommendationConfiguration(toSet(ofNullable(dto.getLimitToGenres())), new UserId(userId)));
		return ResponseEntity.status(200).build();
	}

	private static Set<MovieGenre> toSet(final Optional<List<String>> limitToGenres) {
		final List<String> genres = limitToGenres.orElseGet(Collections::emptyList);
		final Set<MovieGenre> genresFromDto;
		genresFromDto = genres.stream().map(MovieGenre::valueOf).collect(Collectors.toSet());
		return genresFromDto;
	}

	@Data
	private static class UpdateRecommendationConfigurationDto {

		private List<String> limitToGenres;

	}

}
