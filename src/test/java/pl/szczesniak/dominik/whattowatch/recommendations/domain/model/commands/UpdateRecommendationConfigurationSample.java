package pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands;

import lombok.Builder;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Set;

import static java.util.Optional.ofNullable;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class UpdateRecommendationConfigurationSample {

	@Builder
	private static UpdateRecommendationConfiguration build(final Set<MovieGenre> genreNames, final UserId userId) {
		return new UpdateRecommendationConfiguration(
				ofNullable(genreNames).orElse(Set.of()),
				ofNullable(userId).orElse(createAnyUserId())
		);
	}

}
