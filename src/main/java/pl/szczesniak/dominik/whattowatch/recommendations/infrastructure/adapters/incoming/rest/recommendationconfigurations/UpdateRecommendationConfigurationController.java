package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendationconfigurations;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationFacade;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands.UpdateRecommendationConfiguration;
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
public class UpdateRecommendationConfigurationController {

	private final RecommendationFacade facade;
	private final LoggedInUserProvider loggedInUserProvider;

	@PutMapping("/api/users/recommendations/configuration")
	@PreAuthorize("hasAnyRole('USER')")
	public ResponseEntity<?> updateRecommendationConfiguration(@AuthenticationPrincipal final UserDetails userDetails,
															   @RequestBody final UpdateRecommendationConfigurationDto dto) {
		final UserId userId = loggedInUserProvider.getLoggedUser(new Username(userDetails.getUsername()));
		facade.update(
				new UpdateRecommendationConfiguration(toSet(ofNullable(dto.getLimitToGenres())), userId));
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
