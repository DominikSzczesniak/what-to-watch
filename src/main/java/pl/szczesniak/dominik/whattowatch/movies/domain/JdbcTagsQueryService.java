package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagLabel;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

//@Repository
@RequiredArgsConstructor
public class JdbcTagsQueryService implements TagsQuery {

	private final JdbcTemplate jdbcTemplate;


	@Override
	public List<MovieTag> findByUserId(final Integer userId) {
		final String sqlQuery = "SELECT tag_id, tag_label, tag_user_id FROM tags WHERE tag_user_id = ?";
		return jdbcTemplate.query(sqlQuery, new Object[]{userId}, (rs, rowNum) -> getMovieTag(rs));
	}

	@Override
	public Optional<MovieTag> findTagByTagId(final String tagId) {
		final String sqlQuery = "SELECT tag_id, tag_label, tag_user_id FROM tags WHERE tag_id = ?::uuid";
		final List<MovieTag> result = jdbcTemplate.query(sqlQuery, new Object[]{UUID.fromString(tagId)}, (rs, rowNum) -> getMovieTag(rs));
		return result.stream().findFirst();
	}

	private static MovieTag getMovieTag(final ResultSet rs) throws SQLException {
		final MovieTagId foundTagId = new MovieTagId(UUID.fromString(rs.getString("tag_id")));
		final MovieTagLabel tagLabel = new MovieTagLabel(rs.getString("tag_label"));
		final UserId user = new UserId(rs.getInt("tag_user_id"));

		return new MovieTag(foundTagId, tagLabel, user);
	}

	private boolean checkTagIdBelongsToUser(final String tagId, final Integer userId) {
		final String checkTagIdBelongsToUserQuery = "SELECT COUNT(*) FROM tags WHERE tag_id = ?::uuid AND tag_user_id = ?";
		Integer numberOfResultRows = jdbcTemplate.queryForObject(
				checkTagIdBelongsToUserQuery,
				new Object[]{UUID.fromString(tagId), userId},
				Integer.class);

		return Objects.requireNonNullElse(numberOfResultRows, 0) > 0;
	}

	//		if (!checkTagIdBelongsToUser(tags, userId)) {
//			return Collections.emptyList();
//		}
//
//		final String findMoviesByTagIdQuery =
//				"SELECT movie_id, movie_title, user_id FROM movie m " +
//						"JOIN movie_tags mt on m.movie_id = mt.movies_movie_id " +
//						"WHERE tags_tag_id = ?::uuid";
//		return jdbcTemplate.query(findMoviesByTagIdQuery, new Object[]{UUID.fromString(tags)}, (rs, rowNum) ->
//				new Movie(
//						new UserId(rs.getInt("user_id")),
//						new MovieTitle(rs.getString("movie_title"))
//				) {{
//					setMovieId(rs.getInt("movie_id")); // Set the movieId explicitly
//				}});
//		return null;

}