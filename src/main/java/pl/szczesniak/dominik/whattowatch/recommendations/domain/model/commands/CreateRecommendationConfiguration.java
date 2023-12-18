package pl.szczesniak.dominik.whattowatch.recommendations.domain.model.commands;

import lombok.NonNull;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Set;

@Value
public class CreateRecommendationConfiguration {

	@NonNull Set<MovieGenre> limitToGenres;
	@NonNull UserId userId;

}
