package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.outgoing.query;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.model.MovieGenre;
import pl.szczesniak.dominik.whattowatch.recommendations.query.RecommendationConfigurationQueryService;
import pl.szczesniak.dominik.whattowatch.recommendations.query.model.RecommendationConfigurationRequestResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class JdbcRecommendationConfigurationQueryService implements RecommendationConfigurationQueryService {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public Optional<RecommendationConfigurationRequestResult> findRecommendationConfigurationQueryResultBy(final UserId userId) {
		final String sql = "SELECT r.user_id, r.id, c.genre " +
				"FROM recommendation_configuration r " +
				"JOIN configuration_genres c ON r.id = c.configuration_id " +
				"WHERE r.user_id = :userId";

		final MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId.getValue());

		final List<RecommendationConfigurationRequestResult> results = namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
			final RecommendationConfigurationRequestResult result = new RecommendationConfigurationRequestResult(
					rs.getInt("user_id"),
					rs.getInt("id"),
					new HashSet<>()
			);
			fillGenres(rs, result.getGenres());

			return result;
		});

		return results.stream().findFirst();
	}

	private void fillGenres(final ResultSet rs, final Set<MovieGenre> genres) throws SQLException {
		do {
			final String genre = rs.getString("genre");
			genres.add(MovieGenre.valueOf(genre));
		} while (rs.next());
	}


	@Override
	public List<UserId> findAllUsersWithRecommendationConfigurations() {
		final String sql = "SELECT DISTINCT user_id " +
				"FROM recommendation_configuration";

		final List<Integer> result = namedParameterJdbcTemplate.queryForList(sql, Collections.emptyMap(), Integer.class);

		final List<UserId> users = result.stream().map(UserId::new).toList();

		return users;
	}

}
