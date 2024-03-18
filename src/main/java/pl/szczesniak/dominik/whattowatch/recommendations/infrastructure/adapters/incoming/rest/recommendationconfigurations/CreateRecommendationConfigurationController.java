package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendationconfigurations;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationFacade;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.ConfigurationId;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.CreateRecommendationConfiguration;
import pl.szczesniak.dominik.whattowatch.security.LoggedInUserProvider;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@RestController
public class CreateRecommendationConfigurationController {

	private final RecommendationFacade facade;
	private final LoggedInUserProvider loggedInUserProvider;

	@PostMapping("/api/users/recommendations/configuration")
	public ResponseEntity<?> createRecommendationConfiguration(@AuthenticationPrincipal final UserDetails userDetails,
															   @RequestBody final RecommendationConfigurationDto dto) {
		final UserId userId = loggedInUserProvider.getLoggedUser(new Username(userDetails.getUsername()));
		final ConfigurationId configurationId = facade.create(
				new CreateRecommendationConfiguration(toSet(ofNullable(dto.getLimitToGenres())), userId));
		return ResponseEntity.status(201).body(configurationId.getValue());
	}

	private static Set<MovieGenre> toSet(final Optional<List<String>> limitToGenres) {
		final List<String> genres = limitToGenres.orElseGet(Collections::emptyList);
		final Set<MovieGenre> genresFromDto;
		genresFromDto = genres.stream().map(MovieGenre::valueOf).collect(Collectors.toSet());
		return genresFromDto;
	}

	@Data
	private static class RecommendationConfigurationDto {

		private List<String> limitToGenres;

	}

}
