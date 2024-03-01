package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.outgoing.query;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.query.MoviesQueryService;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieCommentQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieInListQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieTagQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.query.model.PagedMovies;
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
public class JdbcMoviesToWatchQueryService implements MoviesQueryService {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public PagedMovies getMoviesToWatch(final UserId userId, final Integer page, final Integer moviesPerPage) {
		final String sql = "SELECT m.id AS movie_id, m.movie_title " +
				"FROM movie m " +
				"WHERE m.user_id = :userId " +
				"ORDER BY m.id " +
				"LIMIT :limit OFFSET :offset";

		final String countMoviesSql = "SELECT COUNT(*) " +
				"FROM movie " +
				"WHERE user_id = :userId";


		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId.getValue());
		params.addValue("limit", moviesPerPage);
		params.addValue("offset", (page - 1) * moviesPerPage);

		final PaginationInfo paginationInfo = getPaginationInfo(moviesPerPage, countMoviesSql, params);

		final List<MovieInListQueryResult> movies = getMovieInListQueryResults(sql, params);


		return new PagedMovies(movies, page, paginationInfo.getTotalPages(), paginationInfo.getTotalMovies());
	}

	@Override
	public PagedMovies getMoviesByTags(final List<MovieTagId> tags, final UserId userId, final Integer page, final Integer moviesPerPage) {
		final String selectMoviesSql = "SELECT m.id, m.movie_title, m.user_id " +
	public List<MovieInListQueryResult> getMoviesToWatch(final UserId userId) {
		final String sql = "SELECT m.id, m.movie_title " +
				"FROM movie m " +
				"WHERE m.user_id = :userId";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId.getValue());

		return getMovies(sql, params);
	}

	@Override
	public List<MovieInListQueryResult> getMoviesByTags(final List<MovieTagId> tags, final UserId userId) {
		final String sql = "SELECT m.id, m.movie_title, m.user_id " +
				"FROM movie m " +
				"JOIN movie_tags mt ON m.id = mt.movie_id " +
				"JOIN tags t ON mt.tags_tag_id = t.tag_id " +
				"WHERE t.tag_id IN (:tagIds) AND m.user_id = :userId " +
				"GROUP BY m.id, m.movie_title, m.user_id " +
				"HAVING COUNT(DISTINCT t.tag_id) = :tagCount " +
				"ORDER BY m.id " +
				"LIMIT :limit OFFSET :offset";

		final String countMoviesSql = "SELECT COUNT(DISTINCT m.id) " +
				"FROM movie m " +
				"JOIN movie_tags mt ON m.id = mt.movie_id " +
				"JOIN tags t ON mt.tags_tag_id = t.tag_id " +
				"WHERE t.tag_id IN (:tagIds) AND m.user_id = :userId";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId.getValue());
		params.addValue("tagCount", tags.size());
		params.addValue("tagIds", tags.stream().map(MovieTagId::getValue).collect(Collectors.toList()));
		params.addValue("limit", moviesPerPage);
		params.addValue("offset", (page - 1) * moviesPerPage);

		final List<MovieInListQueryResult> movies = getMovieInListQueryResults(selectMoviesSql, params);

		final PaginationInfo paginationInfo = getPaginationInfo(moviesPerPage, countMoviesSql, params);

		return new PagedMovies(movies, page, paginationInfo.getTotalPages(), paginationInfo.getTotalMovies());
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

		return jdbcTemplate.query(sql, params, rs -> {
			if (rs.next()) {
				final MovieQueryResult result = new MovieQueryResult(
						rs.getInt("movie_id"),
						rs.getString("movie_title"),
						new HashSet<>(),
						new HashSet<>()
				);

				fillComments(rs, result.getComments());
				fillTags(rs, result.getTags());

				return Optional.of(result);
			} else {
				return Optional.empty();
			}
		});
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

	@Override
	public Optional<MovieTagQueryResult> getTagByTagId(final MovieTagId tagId) {
		final String sql = "SELECT tag_id, tag_label, tag_user_id " +
				"FROM tags " +
				"WHERE tag_id = :tagId";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("tagId", tagId.getValue());

		return jdbcTemplate.query(sql, params, rs -> {
			if (rs.next()) {
				final MovieTagQueryResult result = new MovieTagQueryResult(
						rs.getString("tag_id"),
						rs.getString("tag_label"),
						rs.getInt("tag_user_id")
				);

				return Optional.of(result);
			} else {
				return Optional.empty();
			}
		});
	}

	@Override
	public List<MovieTagQueryResult> getMovieTagsByUserId(final Integer userId) {
		final String sql = "SELECT tag_id, tag_label, tag_user_id " +
				"FROM tags " +
				"WHERE tag_user_id = :userId";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId);

		return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
			final MovieTagQueryResult movieTagQueryResult = new MovieTagQueryResult(
					rs.getString("tag_id"),
					rs.getString("tag_label"),
					rs.getInt("tag_user_id")
			);
			return movieTagQueryResult;
		});
	}

	private List<MovieInListQueryResult> getMovieInListQueryResults(final String sql, final MapSqlParameterSource params) {
		final List<MovieInListQueryResult> movies = jdbcTemplate.query(sql, params, (rs, rowNum) ->
				new MovieInListQueryResult(
						rs.getInt("id"),
						rs.getString("movie_title"))
		);
		return movies;
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
