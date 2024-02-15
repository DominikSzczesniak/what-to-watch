package pl.szczesniak.dominik.whattowatch.movies.infrastructure.query;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieInListQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.WatchedMovieQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DatabaseQueryService implements MoviesQueryService, WatchedMoviesQueryService {

	private final JdbcTemplate jdbcTemplate;

	@Override
	public List<MovieInListQueryResult> getMoviesToWatch(final UserId userId) {
		return null;
	}

	@Override
	public Optional<MovieQueryResult> findMovieQueryResult(final MovieId movieId, final UserId userId) {
		return Optional.empty();
	}

	@Override
	public Optional<MovieTagQueryResult> getTagByTagId(final MovieTagId tagId) {
		return Optional.empty();
	}

	@Override
	public List<MovieInListQueryResult> getMoviesByTags(final List<MovieTagId> tags, final UserId userId) {
		return null;
	}

	@Override
	public List<MovieTagQueryResult> getMovieTagsByUserId(final Integer userId) {
		return null;
	}

	@Override
	public List<WatchedMovieQueryResult> getWatchedMovies(final UserId userId) {
		return null;
	}
}
