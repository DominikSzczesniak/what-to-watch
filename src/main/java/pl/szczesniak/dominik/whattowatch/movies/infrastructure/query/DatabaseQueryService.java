package pl.szczesniak.dominik.whattowatch.movies.infrastructure.query;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieCommentQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieInListQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.WatchedMovieQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DatabaseQueryService implements MoviesQueryService, WatchedMoviesQueryService {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public List<MovieInListQueryResult> getMoviesToWatch(final UserId userId) {
		final String sql = "SELECT " +
				"m.id AS movie_id, " +
				"m.movie_title " +
				"FROM " +
				"movie m " +
				"WHERE " +
				"m.user_id = :userId";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId.getValue());

		return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
			final MovieInListQueryResult movieQueryResult = new MovieInListQueryResult(
					rs.getInt("movie_id"),
					rs.getString("movie_title")
			);

			return movieQueryResult;
		});
	}

	@Override
	public Optional<MovieQueryResult> findMovieQueryResult(final MovieId movieId, final UserId userId) {
		final String sql = "SELECT m.id AS movie_id, m.movie_title, mc.comment_id, " +
				"mc.text, t.tag_id, t.tag_label, t.tag_user_id " +
				"FROM movie m " +
				"LEFT JOIN movie_comment mc ON m.id = mc.movie_id " +
				"LEFT JOIN movie_tags mt ON m.id = mt.movie_id " +
				"LEFT JOIN tags t ON mt.tags_tag_id = t.tag_id " +
				"WHERE m.id = :movieId AND m.user_id = :userId";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("movieId", movieId.getValue());
		params.addValue("userId", userId.getValue());

		try {
			final MovieQueryResult movieQueryResult = namedParameterJdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
				final MovieQueryResult result = new MovieQueryResult(
						rs.getInt("movie_id"),
						rs.getString("movie_title"),
						new HashSet<>(),
						new HashSet<>()
				);

				fillComments(rs, result.getComments());
				fillTags(rs, result.getTags());
				return result;
			});

			return Optional.of(movieQueryResult);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	private static void fillComments(final ResultSet rs, final Set<MovieCommentQueryResult> comments) throws SQLException {
		if (rs.getString("comment_id") != null) {
			comments.add(
					new MovieCommentQueryResult(
							rs.getString("comment_id"),
							rs.getString("text")
					));
		}
	}

	private static void fillTags(final ResultSet rs, final Set<MovieTagQueryResult> movieQueryResult) throws SQLException {
		if (rs.getString("tag_id") != null) {
			movieQueryResult.add(
					new MovieTagQueryResult(
							rs.getString("tag_id"),
							rs.getString("tag_label"),
							rs.getInt("tag_user_id")
					)
			);
		}
	}

	public List<MovieInListQueryResult> getMoviesByTags(final List<MovieTagId> tags, final UserId userId) {
		final String sql = "SELECT m.id, m.movie_title, m.user_id " +
				"FROM movie m " +
				"JOIN movie_tags mt ON m.id = mt.movie_id " +
				"JOIN tags t ON mt.tags_tag_id = t.tag_id " +
				"WHERE t.tag_id IN (:tagIds) AND m.user_id = :userId " +
				"GROUP BY m.id, m.movie_title, m.user_id " +
				"HAVING COUNT(DISTINCT t.tag_id) = :tagCount";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId.getValue());
		params.addValue("tagCount", tags.size());
		params.addValue("tagIds", tags.stream().map(MovieTagId::getValue).collect(Collectors.toList()));

		return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) ->
				new MovieInListQueryResult(
						rs.getInt("id"),
						rs.getString("movie_title"))
		);
	}

	@Override
	public Optional<MovieTagQueryResult> getTagByTagId(final MovieTagId tagId) {
		final String sql = "SELECT " +
				"tag_id, " +
				"tag_label, " +
				"tag_user_id " +
				"FROM " +
				"tags " +
				"WHERE " +
				"tag_id = :tagId";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("tagId", tagId.getValue());

		try {
			final MovieTagQueryResult queryResult = namedParameterJdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
				final MovieTagQueryResult movieTagQueryResult = new MovieTagQueryResult(
						rs.getString("tag_id"),
						rs.getString("tag_label"),
						rs.getInt("tag_user_id")
				);
				return movieTagQueryResult;
			});
			return Optional.of(queryResult);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<MovieTagQueryResult> getMovieTagsByUserId(final Integer userId) {
		final String sql = "SELECT " +
				"tag_id, " +
				"tag_label, " +
				"tag_user_id " +
				"FROM " +
				"tags " +
				"WHERE " +
				"tag_user_id = :userId";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId);

		return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
			final MovieTagQueryResult movieTagQueryResult = new MovieTagQueryResult(
					rs.getString("tag_id"),
					rs.getString("tag_label"),
					rs.getInt("tag_user_id")
			);
			return movieTagQueryResult;
		});
	}

	@Override
	public List<WatchedMovieQueryResult> getWatchedMovies(final UserId userId) {
		final String sql = "SELECT " +
				"m.movie_id, " +
				"m.movie_title, " +
				"m.user_id " +
				"FROM " +
				"watched_movie m " +
				"WHERE " +
				"m.user_id = :userId";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId.getValue());

		return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
			final WatchedMovieQueryResult watchedMovieQueryResult = new WatchedMovieQueryResult(
					rs.getInt("movie_id"),
					rs.getString("movie_title"),
					rs.getInt("user_id")
			);
			return watchedMovieQueryResult;
		});
	}

}
