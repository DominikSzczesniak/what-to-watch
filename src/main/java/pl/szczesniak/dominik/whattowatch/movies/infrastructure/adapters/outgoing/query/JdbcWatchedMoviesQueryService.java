package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.query;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import pl.szczesniak.dominik.whattowatch.movies.query.WatchedMoviesQueryService;
import pl.szczesniak.dominik.whattowatch.movies.query.model.PagedWatchedMovies;
import pl.szczesniak.dominik.whattowatch.movies.query.model.WatchedMovieQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JdbcWatchedMoviesQueryService implements WatchedMoviesQueryService {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public PagedWatchedMovies getWatchedMovies(final UserId userId, final Integer page, final Integer moviesPerPage) {
		final String selectMoviesSql = "SELECT m.movie_id, m.movie_title, m.user_id " +
				"FROM watched_movie m " +
				"WHERE m.user_id = :userId " +
				"LIMIT :limit OFFSET :offset";

		final String countMoviesSql = "SELECT COUNT(*) " +
				"FROM watched_movie m " +
				"WHERE m.user_id = :userId";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId.getValue());
		params.addValue("limit", moviesPerPage);
		params.addValue("offset", (page - 1) * moviesPerPage);

		final List<WatchedMovieQueryResult> movies = jdbcTemplate.query(selectMoviesSql, params, (rs, rowNum) -> {
			final WatchedMovieQueryResult watchedMovieQueryResult = new WatchedMovieQueryResult(
					rs.getInt("movie_id"),
					rs.getString("movie_title"),
					rs.getInt("user_id")
			);
			return watchedMovieQueryResult;
		});

		final PaginationInfo paginationInfo = getPaginationInfo(moviesPerPage, countMoviesSql, params);

		return new PagedWatchedMovies(movies, page, paginationInfo.getTotalPages(), paginationInfo.getTotalMovies());
	}

	private PaginationInfo getPaginationInfo(final Integer moviesPerPage, final String countMoviesSql, final MapSqlParameterSource params) {
		int totalMovies = 0;
		int totalPages = 0;

		final List<Integer> result = jdbcTemplate.query(countMoviesSql, params, (resultSet, i) -> resultSet.getInt(1));
		if (!result.isEmpty()) {
			totalMovies = result.get(0);
			totalPages = (int) Math.ceil((double) totalMovies / moviesPerPage);
		}

		return new PaginationInfo(totalMovies, totalPages);
	}

	@Value
	private static class PaginationInfo {
		Integer totalMovies;
		Integer totalPages;
	}

}
