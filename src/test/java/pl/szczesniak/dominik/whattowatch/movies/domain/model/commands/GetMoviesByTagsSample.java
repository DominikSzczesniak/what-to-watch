package pl.szczesniak.dominik.whattowatch.movies.domain.model.commands;

import lombok.Builder;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

import static java.util.Optional.ofNullable;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.TagIdSample.createAnyTagId;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class GetMoviesByTagsSample {

	private static final int CURRENT_PAGE = 0;
	private static final int MOVIES_PER_PAGE = 10000000;

	@Builder
	private static GetMoviesByTags build(final UserId userId, final List<MovieTagId> tags, final Integer page, final Integer moviesPerPage) {
		return new GetMoviesByTags(
				ofNullable(tags).orElse(List.of(createAnyTagId(), createAnyTagId(), createAnyTagId())),
				ofNullable(userId).orElse(createAnyUserId()),
				ofNullable(page).orElse(CURRENT_PAGE),
				ofNullable(moviesPerPage).orElse(MOVIES_PER_PAGE)
		);
	}

}
