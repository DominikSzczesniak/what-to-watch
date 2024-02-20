package pl.szczesniak.dominik.whattowatch.movies.infrastructure.query;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class DatabaseQueryService implements MoviesQueryService, WatchedMoviesQueryService {

	private final JdbcTemplate jdbcTemplate;

	@Override
	public List<MovieInListQueryResult> getMoviesToWatch(final UserId userId) {
		final String sql = "SELECT " +
				"m.id AS movie_id, " +
				"m.movie_title, " +
				"mt.tag_id, " +
				"mt.tag_label, " +
				"mt.tag_user_id " +
				"FROM " +
				"movie m " +
				"LEFT JOIN " +
				"tags mt ON mt.tag_user_id = m.user_id " +
				"WHERE " +
				"m.user_id = ?";

		final List<MovieInListQueryResult> results = jdbcTemplate.query(sql, (rs, rowNum) -> {
			final MovieInListQueryResult movieQueryResult = new MovieInListQueryResult(
					rs.getInt("movie_id"),
					rs.getString("movie_title")
			);

			return movieQueryResult;
		}, userId.getValue());

		return results;
	}

	@Override
	public Optional<MovieQueryResult> findMovieQueryResult(final MovieId movieId, final UserId userId) {
		final String sql = "SELECT " +
				"m.id AS movie_id, " +
				"m.movie_title, " +
				"mc.comment_id, " +
				"mc.text, " +
				"t.tag_id, " +
				"t.tag_label, " +
				"t.tag_user_id " +
				"FROM " +
				"movie m " +
				"LEFT JOIN " +
				"movie_comment mc ON m.id = mc.movie_id " +
				"LEFT JOIN " +
				"movie_tags mt ON m.id = mt.movie_id " +
				"LEFT JOIN " +
				"tags t ON mt.tags_tag_id = t.tag_id " +
				"WHERE " +
				"m.id = ? " +
				"AND " +
				"m.user_id = ?";

		try {
			final MovieQueryResult movieQueryResult = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
				final MovieQueryResult result = new MovieQueryResult(
						rs.getInt("movie_id"),
						rs.getString("movie_title"),
						new HashSet<>(),
						new HashSet<>()
				);

				if (rs.getString("comment_id") != null) {
					result.getComments().add(new MovieCommentQueryResult(
							rs.getString("comment_id"),
							rs.getString("text")
					));
				}

				fillTags(rs, result.getTags());

				return result;
			}, movieId.getValue(), userId.getValue());

			return Optional.of(movieQueryResult);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
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
				"WHERE t.tag_id IN (" +
				tags.stream().map(tag -> "?").collect(Collectors.joining(",")) +
				") AND m.user_id = ? " +
				"GROUP BY m.id, m.movie_title, m.user_id " +
				"HAVING COUNT(DISTINCT t.tag_id) = ?";

		final Object[] params = Stream.concat(tags.stream().map(MovieTagId::getValue),
				Stream.of(userId.getValue(), tags.size())).toArray();

		return jdbcTemplate.query(sql, params, (rs, rowNum) ->
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
				"tag_id = ?";

		try {
			final MovieTagQueryResult query = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
				final MovieTagQueryResult movieTagQueryResult = new MovieTagQueryResult(
						rs.getString("tag_id"),
						rs.getString("tag_label"),
						rs.getInt("tag_user_id")
				);
				return movieTagQueryResult;
			}, tagId.getValue());
			return Optional.of(query);
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
				"tag_user_id = ?";

		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			final MovieTagQueryResult movieTagQueryResult = new MovieTagQueryResult(
					rs.getString("tag_id"),
					rs.getString("tag_label"),
					rs.getInt("tag_user_id")
			);
			return movieTagQueryResult;
		}, userId);
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
				"m.user_id = ?";

		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			final WatchedMovieQueryResult watchedMovieQueryResult = new WatchedMovieQueryResult(
					rs.getInt("movie_id"),
					rs.getString("movie_title"),
					rs.getInt("user_id")
			);
			return watchedMovieQueryResult;
		}, userId.getValue());
	}

}
