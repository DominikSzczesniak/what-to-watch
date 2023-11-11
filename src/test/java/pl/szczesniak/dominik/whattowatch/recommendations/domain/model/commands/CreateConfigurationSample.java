package pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands;

import lombok.Builder;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Set;

import static java.util.Optional.ofNullable;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class CreateConfigurationSample {

	@Builder
	private static CreateRecommendationConfiguration build(final Set<MovieGenre> genreNames, final UserId userId) {
		return new CreateRecommendationConfiguration(
				ofNullable(genreNames).orElse(Set.of()),
				ofNullable(userId).orElse(createAnyUserId())
		);
	}

}
