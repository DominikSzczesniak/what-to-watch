package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.Builder;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import static java.util.Optional.ofNullable;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitleSample.createAnyMovieTitle;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class AddMovieToListSample {

	@Builder
	private static AddMovieToList build(final MovieTitle movieTitle, final UserId userId) {
		return AddMovieToList.builder(
						ofNullable(movieTitle).orElse(createAnyMovieTitle()),
						ofNullable(userId).orElse(createAnyUserId())
				)
				.build();
	}

}