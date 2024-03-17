package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.query;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import pl.szczesniak.dominik.whattowatch.movies.query.WatchedMoviesQueryService;
import pl.szczesniak.dominik.whattowatch.movies.query.model.WatchedMovieQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JdbcWatchedMoviesQueryService implements WatchedMoviesQueryService {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public List<WatchedMovieQueryResult> getWatchedMovies(final UserId userId) {
		final String sql = "SELECT m.movie_id, m.movie_title, m.user_id " +
				"FROM watched_movie m " +
				"WHERE m.user_id = :userId";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId.getValue());

		return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
			final WatchedMovieQueryResult watchedMovieQueryResult = new WatchedMovieQueryResult(
					rs.getInt("movie_id"),
					rs.getString("movie_title"),
					rs.getInt("user_id")
			);

			return watchedMovieQueryResult;
		});
	}

}
