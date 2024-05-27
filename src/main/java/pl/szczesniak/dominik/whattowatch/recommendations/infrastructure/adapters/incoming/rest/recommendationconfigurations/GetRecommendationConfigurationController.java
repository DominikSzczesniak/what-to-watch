package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.rest.recommendationconfigurations;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationFacade;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.query.model.RecommendationConfigurationQueryResult;
import pl.szczesniak.dominik.whattowatch.security.LoggedInUserProvider;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
public class GetRecommendationConfigurationController {

	private final RecommendationFacade facade;
	private final LoggedInUserProvider loggedInUserProvider;

	@GetMapping("/api/recommendations/configuration")
	@PreAuthorize("hasAnyRole('USER')")
	public ResponseEntity<RecommendationConfigurationDto> getRecommendationConfiguration(@AuthenticationPrincipal final UserDetails userDetails) {
		final UserId userId = loggedInUserProvider.getLoggedUser(new Username(userDetails.getUsername()));
		final RecommendationConfigurationQueryResult config = facade.getLatestRecommendationConfiguration(userId);
		final RecommendationConfigurationDto configDto = new RecommendationConfigurationDto(
				config.getConfigurationId(),
				mapGenres(config.getGenres()),
				config.getUserId()
		);
		return ResponseEntity.status(200).body(configDto);
	}

	private List<String> mapGenres(final Set<MovieGenre> genres) {
		return genres.stream().map(Enum::name).toList();
	}

	@Value
	private static class RecommendationConfigurationDto {

		Integer configurationId;
		List<String> genreNames;
		Integer userId;

	}

}
