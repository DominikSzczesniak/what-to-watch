package pl.szczesniak.dominik.whattowatch.movies.domain.model.queries;

import lombok.Builder;
import pl.szczesniak.dominik.whattowatch.movies.query.model.GetWatchedMoviesToWatch;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import static java.util.Optional.ofNullable;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class GetWatchedMoviesToWatchSample {

	private static final int CURRENT_PAGE = 0;
	private static final int MOVIES_PER_PAGE = 10000000;

	@Builder
	private static GetWatchedMoviesToWatch build(final UserId userId, final Integer page, final Integer moviesPerPage) {
		return new GetWatchedMoviesToWatch(
				ofNullable(userId).orElse(createAnyUserId()),
				ofNullable(page).orElse(CURRENT_PAGE),
				ofNullable(moviesPerPage).orElse(MOVIES_PER_PAGE)

		);
	}

}
