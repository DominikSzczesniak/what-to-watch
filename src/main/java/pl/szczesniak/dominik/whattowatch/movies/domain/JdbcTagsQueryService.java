package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.TagLabel;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcTagsQueryService implements TagsQueryService {

	private final JdbcTemplate jdbcTemplate;


	@Override
	public List<MovieTag> findByUserId(final Integer userId) {
		final String sqlQuery = "SELECT tag_id, tag_label, user_id_label FROM tag_table WHERE user_id_label = ?";

		return jdbcTemplate.query(sqlQuery, new Object[]{userId}, (rs, rowNum) -> {
			final TagId tagId = new TagId(UUID.fromString(rs.getString("tag_id")));
			final TagLabel tagLabel = new TagLabel(rs.getString("tag_label"));
			final UserId user = new UserId(rs.getInt("user_id_label"));

			return new MovieTag(tagId, tagLabel, user);
		});
	}

	@Override
	public Optional<MovieTag> findTagByTagId(final String tagId) {
		final String sqlQuery = "SELECT tag_id, tag_label, user_id_label FROM tag_table WHERE tag_id = ?::uuid";
		List<MovieTag> result = jdbcTemplate.query(sqlQuery, new Object[]{UUID.fromString(tagId)}, (rs, rowNum) -> {
			final TagId foundTagId = new TagId(UUID.fromString(rs.getString("tag_id")));
			final TagLabel tagLabel = new TagLabel(rs.getString("tag_label"));
			final UserId user = new UserId(rs.getInt("user_id_label"));

			return new MovieTag(foundTagId, tagLabel, user);
		});

		return result.stream().findFirst();
	}

}